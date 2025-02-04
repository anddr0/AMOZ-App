package com.example.amoz.ui.screens.bottom_screens.additional_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.amoz.R

@Composable
fun FAQScreen(navController: NavController, paddingValues: PaddingValues) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {
        val faqMap = mapOf(
            stringResource(R.string.faq_question_1) to stringResource(R.string.faq_answer_1),
            stringResource(R.string.faq_question_2) to stringResource(R.string.faq_answer_2),
            stringResource(R.string.faq_question_3) to stringResource(R.string.faq_answer_3),
            stringResource(R.string.faq_question_4) to stringResource(R.string.faq_answer_4),
            stringResource(R.string.faq_question_5) to stringResource(R.string.faq_answer_5),
            stringResource(R.string.faq_question_6) to stringResource(R.string.faq_answer_6),
            stringResource(R.string.faq_question_7) to stringResource(R.string.faq_answer_7),
            stringResource(R.string.faq_question_8) to stringResource(R.string.faq_answer_8),
            stringResource(R.string.faq_question_9) to stringResource(R.string.faq_answer_9),
            stringResource(R.string.faq_question_10) to stringResource(R.string.faq_answer_10)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            faqMap.forEach{ (question, answer) ->
                FaqItem(question,answer)
            }
        }
    }
}

