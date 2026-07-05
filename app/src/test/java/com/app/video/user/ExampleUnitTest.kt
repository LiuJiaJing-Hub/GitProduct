package com.app.video.user

import com.app.video.user.core.util.Md5Util
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun md5_isGenerated() {
        val result = Md5Util.md5("app-video-user")
        assertEquals(32, result.length)
        assertTrue(result.isNotBlank())
    }
}
