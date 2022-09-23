package com.geeksquad.td

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.geeksquad.td.ui.theme.TDForAdultsTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var games: Array<String>
    private lateinit var topiclist: Array<String>
    private lateinit var choicelist: Array<String>
    private lateinit var truth: ArrayList<String>
    private lateinit var dare: ArrayList<String>
    private lateinit var players: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        games = arrayOf("Truth", "Dare")
        players = ArrayList()
        truth = ArrayList()
        dare = ArrayList()
        topiclist = resources.getStringArray(R.array.topic)
        choicelist = resources.getStringArray(R.array.choice)
        seperate(topiclist, choicelist)
        setContent {
            TDForAdultsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    addplayers(LocalContext.current)
                    UI(LocalContext.current)
                }
            }
        }
    }

    @Composable
    fun UI(context: Context){

        var choice by remember{
            mutableStateOf("Tap To Choose Truth or Dare!")
        }

        var name by remember {
            mutableStateOf("Tap To Select Player!")
        }

        var topic by remember {
            mutableStateOf("Your Topic Comes Here!")
        }

        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                name = players.random()
            }){
                Column(Modifier.padding(50.dp)) {
                    Text(name, fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Normal,
                        softWrap = true, textAlign = TextAlign.Center, color = Color.Gray
                    )
                }
            }
            Button(onClick = {
                choice = games.random()
                topic = selecttopic(choice)
            }){
                Column(Modifier.padding(50.dp)) {
                    Text(choice, fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Normal,
                        softWrap = true, textAlign = TextAlign.Center, color = Color.Gray
                    )
                }
            }
            Topic(topic = topic)
        }
    }
    
    @Composable
    fun Topic(topic: String){
        Text(topic, fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace, fontStyle = FontStyle.Normal,
            softWrap = true, textAlign = TextAlign.Center, color = Color.Gray
        )
    }

    fun seperate(topics: Array<String>, choices: Array<String>){
        for(i in choices.indices){
            if(choices[i] == "D") dare.add(topics[i])
            else if(choices[i] == "T") truth.add(topics[i])
        }
    }

    fun selecttopic(td: String): String {
        var topic: String = ""
        if(td == games[0]) topic = truth.random()
        else if(td == games[1]) topic = dare.random()
        return topic
    }

    @Composable
    fun addplayers(context: Context){
        var showadd by remember {
            mutableStateOf(true)
        }

        var name by remember {
            mutableStateOf("")
        }

        if(showadd){
            AlertDialog(
                onDismissRequest = {
                    showadd = true
                },
                text = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        placeholder = {
                            Text(text = "Enter Player's Name")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Person Icon"
                            )
                        },
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            name = name.trim()
                            players.add(name)
                            name = ""
                        },
                    ) {
                    Text(text = "Add")
                }},
                dismissButton = {
                    Button(
                        onClick = {
                            if(players.isEmpty()){
                                Toast.makeText(context, "Please Add Some Players To Continue!", Toast.LENGTH_SHORT).show()
                                showadd = true
                            }
                            else showadd = false
                        }) {
                        Text(text = "Start")
                    }
                }
            )
        }
    }
}