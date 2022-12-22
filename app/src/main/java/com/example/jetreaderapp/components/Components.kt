package com.example.jetreaderapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jetreaderapp.R
import com.example.jetreaderapp.model.MBook
import com.example.jetreaderapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        "A. Reader",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.h3,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    InputField(
        modifier,
        emailState,
        labelId,
        enabled,
        imeAction,
        onAction,
        keyBoardType = KeyboardType.Email
    )
}

@Composable
fun InputField(
    modifier: Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    isSingleLine: Boolean = true,
    keyBoardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable () -> Unit = {}
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        maxLines = 1,
        enabled = enabled,
        keyboardActions = onAction,
        keyboardOptions = KeyboardOptions(keyboardType = keyBoardType, imeAction = imeAction),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.onBackground,
        ),
        shape = RoundedCornerShape(15.dp),
        modifier = modifier.padding(10.dp),
        visualTransformation = visualTransformation,
        trailingIcon = {
            trailingIcon()
        }
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    loading : Boolean = false,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    InputField(
        modifier = modifier,
        valueState = passwordState,
        labelId = labelId,
        enabled = loading,
        imeAction = imeAction,
        keyBoardType = KeyboardType.Password,
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
        onAction = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = {
        passwordVisibility.value = !visible
    }) {
        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Switch Password Visbility")
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(
                label, fontSize = 19.sp, fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}

@Composable
fun ReaderAppBar(
    title: String,
    showProfile: Boolean = true,
    icon : ImageVector? = null,
    navController: NavHostController,
    onBackArrowClicked : () -> Unit = {},
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Favorite, contentDescription = "Logo Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.6f)
                    )
                }
                if(icon != null) {
                    Icon(imageVector = icon, contentDescription = "Arrow Back",
                    tint = Color.Red.copy(0.7f), modifier = Modifier.clickable {
                        onBackArrowClicked()
                    })
                }
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    title, color = Color.Red.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold, fontSize = 17.sp
                )

            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(ReaderScreens.LoginScreen.name) {
                        popUpTo(ReaderScreens.ReaderHomeScreen.name) {
                            inclusive = true
                        }
                    }
                }
            }) {
                if(showProfile) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Logout, contentDescription = "Log out",
                            tint = Color.Green.copy(alpha = 0.4f)
                        )
                    }
                } else {
                    Box{}
                }

            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
    )
}

@Composable
fun FabContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onTap()
        },
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "Add a Book",
            tint = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Filled.Star,
                tint = Color.Black,
                contentDescription = "Start", modifier = Modifier.padding(3.dp))
            Text(score.toString(), style = MaterialTheme.typography.subtitle1, color = Color.Black)
        }
    }
}

@Preview
@Composable
fun RoundedButton(
    label : String = "Reading",
    radius : Int = 29,
    onPress : () -> Unit = {},
) {
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xff92CBDF)
    ) {
        Column(modifier = Modifier
            .width(90.dp)
            .height(40.dp)
            .clickable { onPress() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }
}

@Preview
@Composable
fun ListCard(
    book: MBook = MBook("asdf", title = "Wednesday", authors = "Tim Burton", notes = "Hellooo..."),
    onPressDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics

    // Screen width
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable {
                onPressDetails(book.title!!)
            }) {
        Column(modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center) {

                LoadImage()

                Spacer(Modifier.width(50.dp))

                Column(modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,) {
                    Icon(imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favourite Icon",
                        tint = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp))

                    BookRating(score = 3.5)
                }
            }

            Text(book.title!!, modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2, overflow = TextOverflow.Ellipsis)

            Text(book.authors!!, modifier = Modifier.padding(4.dp),
                color = Color.Black,
                style = MaterialTheme.typography.caption)

            Spacer(Modifier.fillMaxHeight(0.15f))

            Row(horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom) {
                Spacer(modifier = Modifier.fillMaxWidth(0.6f))
                RoundedButton(label = "Reading", radius = 70)
            }
        }
    }
}

@Composable
fun LoadImage(url : String = "https://www.sperling.it/content/uploads/2022/09/978882007444HIG.JPG") {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_background),
        contentDescription = "Book Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .clip(CircleShape)
            .height(140.dp)
            .width(100.dp)
            .padding(4.dp)
    )
}