package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.EpochType
import com.example.data.model.QuizCatalog
import com.example.data.model.TechnologyCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Еволюція", appName)
    }

    @Test
    fun `verify five historical epochs and technology catalog`() {
        assertEquals(5, EpochType.entries.size)
        assertTrue(TechnologyCatalog.allTechnologies.isNotEmpty())
        assertTrue(QuizCatalog.epochQuizzes.containsKey(EpochType.DRYOPITHECUS))
    }
}
