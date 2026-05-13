package com.example.laptoparthesis.ui.screens.ar

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ar.core.Anchor
import com.google.ar.core.Frame
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberModelInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArScreen(
    onBackClick: () -> Unit
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val anchors = remember { mutableStateListOf<Anchor>() }
    var lastFrame by remember { mutableStateOf<Frame?>(null) }
    
    // Using the local ASUSLaptop model from assets.
    val modelAssetPath = "models/ASUSLaptop.glb"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AR Viewer") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ARSceneView(
                modifier = Modifier.fillMaxSize(),
                engine = engine,
                modelLoader = modelLoader,
                planeRenderer = true,
                onSessionUpdated = { _, frame ->
                    lastFrame = frame
                },
                onTouchEvent = { motionEvent, _ ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        lastFrame?.let { frame ->
                            val hits = frame.hitTest(motionEvent.x, motionEvent.y)
                            hits.firstOrNull()?.let { hit ->
                                anchors.add(hit.createAnchor())
                            }
                        }
                    }
                    false
                }
            ) {
                anchors.forEach { anchor ->
                    AnchorNode(anchor = anchor) {
                        val modelInstance = rememberModelInstance(
                            modelLoader = modelLoader,
                            assetFileLocation = modelAssetPath
                        )
                        if (modelInstance != null) {
                            ModelNode(
                                modelInstance = modelInstance,
                                scaleToUnits = 0.5f,
                                isEditable = true
                            )
                        }
                    }
                }
            }
            
            if (anchors.isEmpty()) {
                Text(
                    text = "Tap on a floor/plane to place the 3D model",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(32.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
