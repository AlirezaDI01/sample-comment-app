package com.example.sample.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sample.data.model.Comment
import com.example.sample.data.remote.NetworkResult
import com.example.sample.domain.LoginState
import com.example.sample.viewModels.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit,
    navigateToWelcomeScreen: () -> Unit
) {

    val loginState by homeViewModel.loginState.collectAsState()

    when (loginState) {
        LoginState.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LoginState.NOT_LOGGED_IN -> {
            navigateToWelcomeScreen()
        }

        LoginState.LOGGED_IN -> {
            Home(
                homeViewModel = homeViewModel,
                navigateToProfileScreen = navigateToProfileScreen
            )
        }
    }

}

@Composable
private fun Home(
    homeViewModel: HomeViewModel,
    navigateToProfileScreen: () -> Unit
) {
    val commentsStatus by homeViewModel.comments.collectAsState()
    val listState = rememberLazyListState()


    val comments = remember(commentsStatus) {
        (commentsStatus as? NetworkResult.Success<List<Comment>>)?.data?.reversed() ?: emptyList()
    }


    //the parent comment for replying
    var parentComment: Comment? by remember {
        mutableStateOf(null)
    }

    //when a new comment is added, the list scrolls to it(bottom of list)
    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            listState.animateScrollToItem(comments.size - 1)
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "کامنت‌ها",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )


            TextButton(
                onClick = navigateToProfileScreen,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(
                    text = "پروفایل",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (commentsStatus is NetworkResult.Loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    state = listState
                ) {
                    items(
                        items = comments,
                        key = { it.id }
                    ) { comment ->
                        Comment(
                            comment = comment,
                            reply = { c -> parentComment = c }
                        )
                    }
                }
            }
        }

        // Add Comment Box
        AddCommentBox(
            modifier = Modifier.fillMaxWidth(),
            parentComment = parentComment,
            clearReply = { parentComment = null },
            onSendComment = { content, parentId ->
                homeViewModel.postComment(comment = content, parentId = parentId)
                parentComment = null
            }
        )
    }
}

@Composable
private fun Comment(
    comment: Comment,
    reply: (comment: Comment) -> Unit
) {

    var showReplies by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {

        Icon(
            imageVector = Icons.Filled.AccountCircle, contentDescription = null,
            tint = MaterialTheme.colorScheme.primary, modifier = Modifier
                .size(32.dp)
                .padding(4.dp)
        )


        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {

            //Username
            Text(
                text = comment.user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )


            //comment content
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                //replying
                TextButton(
                    onClick = {
                        reply(comment)
                    }
                ) {
                    Text(
                        text = "پاسخ دادن",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }

                //show replies
                if (!comment.replies.isNullOrEmpty()) {
                    TextButton(
                        onClick = {
                            showReplies = !showReplies
                        }
                    ) {
                        Text(
                            text = if (!showReplies) "نمایش پاسخ‌ها" else "مخفی کردن پاسخ ها",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }

                HorizontalDivider()
            }

            //replies
            AnimatedVisibility(showReplies && !comment.replies.isNullOrEmpty()) {

                Column {
                    comment.replies?.forEach { childComment ->
                        Comment(
                            comment = childComment,
                            reply = reply
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddCommentBox(
    modifier: Modifier,
    parentComment: Comment?,
    clearReply: () -> Unit,
    onSendComment: (content: String, parentId: Int?) -> Unit
) {

    var content by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp))
    ) {

        //reply box
        AnimatedVisibility(visible = parentComment != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xfffafafa))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "پاسخ به ${parentComment?.user?.name ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                IconButton(
                    onClick = clearReply,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear reply",
                        tint = Color.Gray
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = content,
                onValueChange = {
                    content = it
                },
                modifier = Modifier.weight(1f),
                maxLines = 4,
                placeholder = {
                    Text(text = "نظر خود را وارد کنید", color = Color.Gray)
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                )
            )

            AnimatedVisibility(
                visible = content.isNotBlank(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    onClick = {
                        onSendComment(content, parentComment?.id)
                        content = ""
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "send comment",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

}