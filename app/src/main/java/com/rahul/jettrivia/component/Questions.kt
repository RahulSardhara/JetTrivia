package com.rahul.jettrivia.component

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.jettrivia.model.QuestionItem
import com.rahul.jettrivia.screen.QuestionViewModel
import com.rahul.jettrivia.ui.theme.AppColors

@Composable
fun Questions(paddingValues: PaddingValues, questionViewModel: QuestionViewModel) {
    val questions = questionViewModel.data.value.data?.toMutableList()
    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (questionViewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            Log.e("Questions", "Error fetching questions: ${e.message}")
            null
        }

        if (question != null) {
            QuestionDisplay(paddingValues, questionItem = question, questionIndex = questionIndex, viewModel = questionViewModel) {
                questionIndex.value = questionIndex.value + 1
            }
        } else {
            Text(text = "No Questions Available")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showProgress(score: Int = 12){
    val gradient = Brush.linearGradient(listOf(androidx.compose.ui.graphics.Color(0xFFF95075),
        androidx.compose.ui.graphics.Color(0xFFBE6BE5)))

    val progressFactor by remember(score) {
        mutableFloatStateOf(score*0.005f)
    }

    Row(modifier = Modifier.padding(3.dp).fillMaxWidth().height(45.dp).
    border(width = 4.dp, brush = Brush.linearGradient(colors = listOf(AppColors.Purple80, AppColors.Pink40)),
        shape = RoundedCornerShape(34.dp)).clip(RoundedCornerShape(topEndPercent = 50,bottomEndPercent = 50, topStartPercent = 50, bottomStartPercent=50)).
    background(androidx.compose.ui.graphics.Color.Transparent), verticalAlignment = Alignment.CenterVertically)  {

        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {}, modifier = Modifier.fillMaxWidth(progressFactor).background(brush =gradient), elevation = null, enabled = false, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Transparent, disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent)
        ){
            Text(text = (score*10).toString(),
                modifier = Modifier.clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.white,
                textAlign = TextAlign.Center)
        }

    }
}


@Preview(showBackground = true)
@Composable
fun QuestionDisplay(
    paddingValues: PaddingValues = PaddingValues(),
    questionItem: QuestionItem = QuestionItem(),
    questionIndex: MutableState<Int> = mutableIntStateOf(0),
    viewModel: QuestionViewModel = hiltViewModel(),
    onNextClicked: (Int) -> Unit = {}
) {

    val choiceState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }

    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)

    }

    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        { index ->
            answerState.value = index
            correctAnswerState.value = choiceState[index] == questionItem.answer
            Log.d("Answer", "Selected Answer: ${choiceState[index]}")
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(paddingValues), color = AppColors.mDarkPurple
    ) {

        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {

            if(questionIndex.value >= 3) {
                showProgress(questionIndex.value)
            }
            QuestionTracker(questionNumber = questionIndex.value, totalQuestions = viewModel.data.value.data?.size ?: 0)
            DrawDottedLine(PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))

            Column {
                Text(
                    text = questionItem.question, modifier = Modifier
                        .padding(top = 15.dp)
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f), color = AppColors.white, fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
                //Choices
                choiceState.forEachIndexed { index, answerText ->

                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(width = 4.dp, brush = Brush.linearGradient(colors = listOf(AppColors.Purple80, AppColors.Pink40)), shape = RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 50, bottomStartPercent = 50))
                            .background(color = androidx.compose.ui.graphics.Color(Color.TRANSPARENT)), verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index), onClick = {
                                updateAnswer(index)
                            }, modifier = Modifier.padding(start = 16.dp), colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                selectedColor = if (correctAnswerState.value == true && index == answerState.value) androidx.compose.ui.graphics.Color(Color.GREEN).copy(alpha = 0.2f) else androidx.compose.ui.graphics.Color(Color.RED).copy(alpha = 0.2f),
                                unselectedColor = AppColors.gray
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light, fontSize = 16.sp, color =
                                        if (correctAnswerState.value == true && index == answerState.value)
                                            androidx.compose.ui.graphics.Color(Color.GREEN).copy(alpha = 0.2f)
                                        else if (correctAnswerState.value == false && index == answerState.value)
                                            androidx.compose.ui.graphics.Color(Color.RED).copy(alpha = 0.2f)
                                        else
                                            androidx.compose.ui.graphics.Color(Color.WHITE)

                                )
                            ) {
                                append(answerText)
                            }

                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }

                Button(
                    onClick = {
                        onNextClicked(questionIndex.value)
                    }, modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PurpleGrey80)
                ) {
                    Text(text = "Next", color = androidx.compose.ui.graphics.Color(Color.WHITE), modifier = Modifier.padding(8.dp), fontSize = 20.sp, fontWeight = FontWeight.Bold)

                }

            }
        }

    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(color = AppColors.white, start = Offset.Zero, end = Offset(size.width, 0f), pathEffect = pathEffect)
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionTracker(
    questionNumber: Int = 10,
    totalQuestions: Int = 100,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(style = SpanStyle(color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 27.sp)) {
                    append("Question $questionNumber/")
                    withStyle(style = SpanStyle(color = AppColors.white, fontWeight = FontWeight.Light, fontSize = 14.sp)) {
                        append("$totalQuestions")
                    }
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )

}