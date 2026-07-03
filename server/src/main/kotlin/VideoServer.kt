import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket

/**
 * 视频列表 TCP Socket 服务端 —— 按需分页，客户端请求一页返回一页
 *
 * 协议：每行一个 JSON，以 \n 分隔
 *   客户端请求 → {"page": 0}
 *   服务端响应 → {"videos": [...], "isLastPage": false}
 *
 * 启动：直接运行 main()，默认监听 9999 端口
 */
fun main() {
    val port = 9999
    val pageSize = 10

    val allVideos = listOf(
        // === Big Buck Bunny 系列（大兔子卡通）===
        VideoDTO("1", "卡通1：大兔子 360p测试版", "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/360/Big_Buck_Bunny_360_10s_1MB.mp4"),
        VideoDTO("2", "卡通2：大兔子完整版", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
        VideoDTO("3", "卡通3：大兔子 360p高清", "https://www.radiantmediaplayer.com/media/big-buck-bunny-360p.mp4"),
        VideoDTO("4", "卡通4：大兔子 720p", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4"),
        VideoDTO("5", "卡通5：大兔子 720p高清", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4"),
        VideoDTO("6", "卡通6：大兔子 720p超清", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_5mb.mp4"),
        VideoDTO("7", "卡通7：大兔子 720p完整", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_10mb.mp4"),
        VideoDTO("8", "卡通8：大兔子 720p原画", "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_20mb.mp4"),
        VideoDTO("9", "卡通9：大兔子 1080p", "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_1MB.mp4"),

        // === Sintel 系列（辛特尔奇幻动画）===
        VideoDTO("10", "卡通10：辛特尔预告 1080p", "https://download.blender.org/durian/trailer/sintel_trailer-1080p.mp4"),
        VideoDTO("11", "卡通11：辛特尔 2K影院版", "https://download.blender.org/durian/movies/sintel-2048-surround.mp4"),
        VideoDTO("12", "卡通12：辛特尔 720p", "https://download.blender.org/durian/movies/sintel-1280-surround.mp4"),
        VideoDTO("13", "卡通13：辛特尔 标清", "https://download.blender.org/durian/movies/sintel-1024-surround.mp4"),

        // === Tears of Steel 系列（钢铁之泪科幻动画）===
        VideoDTO("14", "卡通14：钢铁之泪 战斗片段", "https://mango.blender.org/wp-content/uploads/2013/05/tears-of-steel-battle-clip-medium.mp4"),
        VideoDTO("15", "卡通15：钢铁之泪 完整版", "https://download.blender.org/mango/movies/tears-of-steel-1080p.mp4"),

        // === 其他开源动画 ===
        VideoDTO("16", "卡通16：大象之梦", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
        VideoDTO("17", "卡通17：Google测试-火焰", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
        VideoDTO("18", "卡通18：Google测试-逃脱", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
        VideoDTO("19", "卡通19：Google测试-乐趣", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
        VideoDTO("20", "卡通20：Google测试-兜风", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"),
    )

    println("=== Video Server starting on port $port ===")

    ServerSocket(port).use { server ->
        println("Server listening, waiting for client connection...")
        while (true) {
            val client = server.accept()
            println("Client connected: ${client.inetAddress.hostAddress}")
            Thread {
                try {
                    client.use { socket ->
                        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                        val writer = OutputStreamWriter(socket.getOutputStream())

                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            val request = JSONObject(line!!)
                            val page = request.getInt("page")

                            val from = page * pageSize
                            val to = minOf(from + pageSize, allVideos.size)
                            val pageVideos = allVideos.subList(from, to)
                            val isLastPage = (page + 1) * pageSize >= allVideos.size

                            val videosArray = JSONArray()
                            for (v in pageVideos) {
                                videosArray.put(JSONObject().apply {
                                    put("id", v.id)
                                    put("title", v.title)
                                    put("videoUrl", v.videoUrl)
                                })
                            }

                            val response = JSONObject().apply {
                                put("videos", videosArray)
                                put("isLastPage", isLastPage)
                            }

                            writer.write(response.toString() + "\n")
                            writer.flush()
                            println("Sent page $page, ${pageVideos.size} videos")
                        }
                    }
                } catch (e: Exception) {
                    println("Client disconnected: ${e.message}")
                }
            }.start()
        }
    }
}

data class VideoDTO(
    val id: String,
    val title: String,
    val videoUrl: String
)
