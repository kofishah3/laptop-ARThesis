# Android Native AR Development Roadmap
## AR-Based Laptop Component Learning Tool

**Timeline:** 1-2 months (Prototype) → 10 months (Full Thesis)
**Target:** Smartphone AR app for laptop component identification & assembly

---

## PART 1: PREREQUISITES & SETUP

### Development Environment Setup

#### Required Software (FREE)
- **Android Studio** (latest version) - IDE
- **JDK 17+** - Java/Kotlin compiler
- **Gradle** - Build system (included with Android Studio)
- **Git** - Version control
- **Blender** (optional but recommended) - 3D model editing
- **Android SDK**
  - Minimum API: Level 24 (Android 7.0)
  - Target API: Level 35 (Android 15)
  - **ARCore requires API 24+**

#### Device Requirements
- **Physical Android device** with ARCore support
  - Most devices from 2017+ have ARCore
  - Check: Play Store → Google Play Services for AR
- **OR:** Android Emulator with ARCore support (tedious to set up, test on real device instead)

#### Installation Steps
```bash
# Install Android Studio (download from developer.android.com)
# Then install via Android Studio SDK Manager:
# - Android 15 SDK
# - Android SDK tools
# - NDK (for native code compilation)
# - Emulator (optional)

# Verify installation
java -version  # Should show JDK 17+
adb --version  # ADB is Android Debug Bridge
```

---

## PART 2: CORE TECH STACK FOR NATIVE ANDROID

### Language & Framework
```yaml
Primary Language: Kotlin
  └── Modern, concise, null-safe
  └── Official Google recommendation for Android

IDE: Android Studio
  └── Best-in-class debugging & profiling
  └── Integrated emulator
  └── Layout preview tools

Minimum Android Version: API 24 (7.0 Nougat)
Target Android Version: API 35 (15)
  └── Ensures compatibility with latest devices
```

### ARCore (Google's AR Framework)
```yaml
ARCore Library: com.google.ar:core
  ├── Environment understanding (plane detection)
  ├── Motion tracking (6DOF - 6 degrees of freedom)
  ├── Light estimation (realistic shadows)
  └── Cloud Anchors (multiplayer/cloud sync - optional)

Sceneform (3D Rendering Engine) - LEGACY APPROACH
  └── Note: Google deprecated Sceneform in 2021
  └── Still usable but not recommended for new projects

Filament (Modern 3D Rendering) - RECOMMENDED
  ├── High-performance rendering
  ├── PBR (Physically-Based Rendering)
  ├── Better memory management
  └── Active development
```

### 3D Model Format & Libraries
```yaml
Recommended Format: .glTF 2.0 (.glb binary)
  ├── Optimal for mobile (smaller file size)
  ├── Wide tooling support
  ├── Good for AR
  └── Alternative: .gltf (larger, text-based)

3D Model Loading:
  ├── Filament for rendering
  ├── gltfio (Google's glTF importer for Filament)
  └── ModelViewer (web component for testing models before deploying)

Materials Library:
  └── Sceneform materials (.sfb format)
  └── Custom Filament materials (HDR images for lighting)
```

### Architecture & UI Framework
```yaml
Architecture Pattern: MVVM
  ├── Model: Data layer (Firebase/local)
  ├── ViewModel: Business logic, data handling
  └── View: UI (Fragments, Activities)

Dependency Injection: Hilt
  └── Official Google framework for DI
  └── Cleaner code, easier testing

UI Framework: Jetpack Compose (modern) OR XML Layouts (traditional)
  ├── Compose: More powerful, faster development
  ├── XML: Familiar, good for traditional apps
  └── Recommendation: Start with XML (simpler), migrate to Compose later

Navigation: Jetpack Navigation Component
  └── Fragment-based navigation
  └── Type-safe routing

Lifecycle Management: Jetpack Lifecycle
  └── Proper cleanup, memory management
  └── Camera/AR session lifecycle handling
```

### Data & Backend
```yaml
Cloud Backend: Firebase (RECOMMENDED for fast prototyping)
  ├── Firestore: Real-time component database
  ├── Cloud Storage: 3D model files (.glb)
  ├── Authentication: Firebase Auth (optional for now)
  ├── Analytics: Track user interactions
  └── Hosting: Static website (docs, guides)

Alternative Backend: Custom Node.js/Spring Boot
  └── More control but slower to prototype
  └── Skip for now, focus on Firebase

Local Storage:
  ├── SharedPreferences: Small settings
  ├── Room Database: Component cache
  └── File Storage: Downloaded .glb models
```

### Camera & Permissions
```yaml
Camera Framework: CameraX
  ├── Modern Camera API
  ├── Handles compatibility issues
  ├── Easy preview + ARCore integration
  └── Better than Camera2 (older API)

Required Permissions:
  ├── android.permission.CAMERA
  ├── android.permission.INTERNET (for Firebase)
  ├── android.permission.ACCESS_FINE_LOCATION (optional, for tracking)
  └── Handle runtime permissions (Android 6.0+)
```

### Networking & HTTP
```yaml
HTTP Client: Retrofit + OkHttp
  ├── Type-safe REST API calls
  ├── Interceptors for logging/auth
  ├── Coroutine support
  └── Perfect for Firebase API calls

Alternative: Firebase SDK handles most HTTP
  └── Simpler if using Firebase
```

### Async Programming
```yaml
Coroutines: Kotlin Coroutines + Flow
  ├── Suspend functions for async work
  ├── Lifecycle-aware (lifecycleScope)
  ├── Better than RxJava for most use cases
  └── Easy error handling

Threading:
  └── Don't block main thread
  └── Always load models/data on background threads
```

---

## PART 3: COMPLETE TECH STACK DIAGRAM

```
┌─────────────────────────────────────────────────────────────┐
│                    ANDROID NATIVE APP                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │           UI LAYER (Jetpack Compose/XML)             │  │
│  │  ├─ ARView (camera preview + 3D overlay)             │  │
│  │  ├─ ComponentListScreen (bottom sheet/menu)          │  │
│  │  ├─ DetailScreen (component specs)                   │  │
│  │  └─ InstructionsScreen (assembly steps)              │  │
│  └──────────────────────────────────────────────────────┘  │
│                           ↓                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │      VIEWMODEL + BUSINESS LOGIC (MVVM)               │  │
│  │  ├─ ARViewModel (session, model state)               │  │
│  │  ├─ ComponentViewModel (list, search, filters)       │  │
│  │  └─ ModelLoaderViewModel (3D asset loading)          │  │
│  └──────────────────────────────────────────────────────┘  │
│                           ↓                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │     ARCORE + RENDERING LAYER                         │  │
│  │  ├─ ARCore Session (plane detection, tracking)       │  │
│  │  ├─ Filament Renderer (3D rendering engine)          │  │
│  │  ├─ gltfio (model loading)                           │  │
│  │  └─ CameraX (camera access)                          │  │
│  └──────────────────────────────────────────────────────┘  │
│                           ↓                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │       DATA LAYER (Repository Pattern)                │  │
│  │  ├─ ComponentRepository (fetch/cache components)     │  │
│  │  ├─ ModelRepository (3D model management)            │  │
│  │  └─ LocalDataStore (Room, SharedPreferences)         │  │
│  └──────────────────────────────────────────────────────┘  │
│                           ↓                                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         EXTERNAL SERVICES                            │  │
│  │  ├─ Firebase Firestore (component database)          │  │
│  │  ├─ Firebase Cloud Storage (.glb files)              │  │
│  │  ├─ Firebase Analytics (usage tracking)              │  │
│  │  └─ Google Play Services (permissions, metrics)      │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## PART 4: DETAILED DEPENDENCIES (build.gradle.kts)

### Essential Libraries

```kotlin
// Android Framework
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("androidx.core:core-ktx:1.13.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// ARCore
implementation("com.google.ar:core:1.42.0")

// 3D Rendering (Filament)
implementation("com.google.android.filament:filament-android:1.48.0")
implementation("com.google.android.filament:gltfio-android:1.48.0")

// Camera
implementation("androidx.camera:camera-core:1.3.0")
implementation("androidx.camera:camera-camera2:1.3.0")
implementation("androidx.camera:camera-lifecycle:1.3.0")

// UI
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

// Dependency Injection (Hilt)
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")

// Data Storage
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Firebase
implementation("com.google.firebase:firebase-bom:32.7.0")
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")
implementation("com.google.firebase:firebase-analytics-ktx")

// JSON Processing
implementation("com.google.code.gson:gson:2.10.1")

// Async (Coroutines)
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

// Logging
implementation("com.jakewharton.timber:timber:5.0.1")

// Image Loading (optional, for thumbnails)
implementation("io.coil-kt:coil:2.6.0")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```

---

## PART 5: PROJECT STRUCTURE

```
LaptopARApp/
├── .git/                              # Git version control
├── .github/
│   └── workflows/                     # CI/CD (GitHub Actions)
│       └── build.yml
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml   # App permissions, activities
│   │   │   │
│   │   │   ├── java/com/example/laptopar/
│   │   │   │   ├── MainActivity.kt    # Entry point
│   │   │   │   │
│   │   │   │   ├── di/
│   │   │   │   │   ├── AppModule.kt   # Firebase, Retrofit setup
│   │   │   │   │   └── RepositoryModule.kt
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── ar/
│   │   │   │   │   │   ├── ArFragment.kt      # Main AR view
│   │   │   │   │   │   ├── ArViewModel.kt     # AR logic
│   │   │   │   │   │   └── ArRenderer.kt      # Filament rendering
│   │   │   │   │   │
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── ComponentListFragment.kt
│   │   │   │   │   │   ├── ComponentViewModel.kt
│   │   │   │   │   │   └── ComponentAdapter.kt
│   │   │   │   │   │
│   │   │   │   │   ├── detail/
│   │   │   │   │   │   ├── ComponentDetailFragment.kt
│   │   │   │   │   │   └── DetailViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── common/
│   │   │   │   │       ├── BaseFragment.kt
│   │   │   │   │       └── PermissionsHelper.kt
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── FirestoreService.kt
│   │   │   │   │   │   └── StorageService.kt
│   │   │   │   │   │
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── ComponentDatabase.kt
│   │   │   │   │   │   ├── ComponentDao.kt
│   │   │   │   │   │   └── PreferencesManager.kt
│   │   │   │   │   │
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── ComponentRepository.kt
│   │   │   │   │       └── ModelRepository.kt
│   │   │   │   │
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Component.kt      # Data classes
│   │   │   │   │   │   ├── Specification.kt
│   │   │   │   │   │   └── ARModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── usecase/
│   │   │   │   │       ├── GetComponentsUseCase.kt
│   │   │   │   │       ├── LoadModelUseCase.kt
│   │   │   │   │       └── PlaceModelUseCase.kt
│   │   │   │   │
│   │   │   │   ├── ar/
│   │   │   │   │   ├── ArSessionManager.kt
│   │   │   │   │   ├── ModelPlacementManager.kt
│   │   │   │   │   └── GestureDetector.kt    # Touch interactions
│   │   │   │   │
│   │   │   │   └── util/
│   │   │   │       ├── Extensions.kt
│   │   │   │       ├── Constants.kt
│   │   │   │       └── FileHelper.kt
│   │   │   │
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       │   ├── activity_main.xml
│   │   │       │   ├── fragment_ar.xml
│   │   │       │   ├── fragment_component_list.xml
│   │   │       │   ├── item_component.xml
│   │   │       │   └── fragment_detail.xml
│   │   │       │
│   │   │       ├── values/
│   │   │       │   ├── strings.xml
│   │   │       │   ├── colors.xml
│   │   │       │   ├── dimens.xml
│   │   │       │   └── themes.xml
│   │   │       │
│   │   │       ├── drawable/
│   │   │       │   └── ic_*.xml (icons)
│   │   │       │
│   │   │       └── menu/
│   │   │           └── component_menu.xml
│   │   │
│   │   ├── test/
│   │   │   └── java/.../
│   │   │       ├── data/
│   │   │       │   └── RepositoryTest.kt
│   │   │       └── ui/
│   │   │           └── ViewModelTest.kt
│   │   │
│   │   └── androidTest/
│   │       └── java/.../
│   │           └── ui/
│   │               └── ArFragmentTest.kt
│   │
│   ├── build.gradle.kts              # App-level build config
│   └── google-services.json           # Firebase config (generated)
│
├── build.gradle.kts                   # Project-level build config
├── settings.gradle.kts                # Module config
├── gradle.properties                  # Gradle settings
│
└── README.md                          # Project documentation
```

---

## PART 6: DEVELOPMENT PHASES (1-2 Month Prototype)

### Phase 1: Setup & Infrastructure (Week 1)

**Goals:**
- Project initialization
- Firebase setup
- Basic app structure

**Tasks:**
```
✓ Create Android Studio project (Kotlin, min API 24)
✓ Initialize Git repository
✓ Add Firebase to project
  - Download google-services.json
  - Configure Firestore database
  - Set up Cloud Storage bucket
✓ Set up Hilt dependency injection
✓ Create basic project structure (folders above)
✓ Add all dependencies to build.gradle.kts
```

**Deliverables:**
- Compiled app that runs on device (even if blank)
- Firebase project with database/storage ready
- Git repository with initial commit

---

### Phase 2: Basic UI & Navigation (Week 1-2)

**Goals:**
- App shell with navigation
- Component list UI
- Detail view UI

**Tasks:**
```
✓ Create MainActivity with Fragment container
✓ Set up Jetpack Navigation
✓ Build ComponentListFragment
  - RecyclerView for component list
  - Search/filter functionality
✓ Build ComponentDetailFragment
  - Display component specs
  - Show model preview (non-AR)
✓ Create ArFragment (empty for now, just camera preview)
✓ Build bottom sheet/menu for component selection
✓ Handle camera permissions
```

**Deliverables:**
- Navigable app with 3 screens
- Empty but functional UI
- Camera permissions working

**Sample Code:**
```kotlin
// MainActivity.kt
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// ComponentListFragment.kt
@AndroidEntryPoint
class ComponentListFragment : Fragment() {
    private val viewModel: ComponentViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeComponents()
    }
    
    private fun observeComponents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.components.collect { components ->
                adapter.submitList(components)
            }
        }
    }
}
```

---

### Phase 3: 3D Models & Asset Management (Week 2-3)

**Goals:**
- Download laptop component models
- Optimize for mobile
- Store in Firebase
- Load locally

**Tasks:**
```
✓ Find 3-5 laptop component models
  - GPU, RAM, SSD, Cooling Fan, CPU
  - Download from Sketchfab (free, check licenses)
✓ Optimize models in Blender
  - Reduce polygon count (target: <50K per model)
  - Ensure scale is consistent
  - Export as .glb (binary glTF 2.0)
✓ Upload optimized models to Firebase Cloud Storage
  - /models/gpu/nvidia-rtx-4050.glb
  - /models/ram/corsair-ddr5.glb
  - etc.
✓ Create ModelRepository for download/caching
✓ Implement local file storage
  - Save models to app's cache directory
  - Check local before downloading
```

**Deliverables:**
- 3-5 optimized .glb model files
- Models uploaded to Firebase
- ModelRepository with download/cache logic

**Code Structure:**
```kotlin
// ModelRepository.kt
class ModelRepository @Inject constructor(
    private val storageService: StorageService,
    private val fileHelper: FileHelper
) {
    suspend fun getModel(modelId: String): File {
        // Check local cache first
        fileHelper.getModelFile(modelId)?.let { return it }
        
        // Download from Firebase
        val uri = storageService.downloadModel(modelId)
        return fileHelper.saveModel(modelId, uri)
    }
}
```

---

### Phase 4: ARCore Integration (Week 3-4)

**Goals:**
- Get ARCore running
- Detect planes (floor/table)
- Basic session management

**Tasks:**
```
✓ Implement ArSessionManager
  - Create ARCore session
  - Handle lifecycle (pause/resume)
  - Detect planes
  - Handle permissions
✓ Set up camera preview for AR
  - CameraX + ARCore integration
  - Render camera feed to TextureView
✓ Implement hit testing
  - Detect when user taps screen
  - Determine if tap hits plane
✓ Create ArViewModel
  - Manage AR state
  - Expose planes to UI
```

**Deliverables:**
- App detects planes (floor, table)
- Camera preview working
- Console logs show ARCore running

**Critical Code:**
```kotlin
// ArSessionManager.kt
class ArSessionManager(context: Context) {
    private val session = Session(context)
    
    fun update(): Frame {
        // Update AR session
        return session.update()
    }
    
    fun getPlanes(): Collection<Plane> {
        return session.getAllTrackables(Plane::class.java)
    }
}

// ArFragment.kt (simplified)
@AndroidEntryPoint
class ArFragment : Fragment() {
    private val viewModel: ArViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArSession()
        setupRenderer()
    }
    
    private fun setupArSession() {
        // Initialize ARCore
    }
    
    private fun setupRenderer() {
        // Initialize Filament
    }
}
```

---

### Phase 5: 3D Rendering with Filament (Week 4-5)

**Goals:**
- Render 3D models in AR scene
- Place on detected planes
- Basic lighting

**Tasks:**
```
✓ Set up Filament renderer
  - Create Engine, Renderer, Scene, View
  - Load lighting environment
✓ Implement model loading with gltfio
  - Load .glb files into Filament
  - Handle textures, materials
✓ Create ModelPlacementManager
  - Position models on planes
  - Handle scaling
  - Proper transformations
✓ Implement basic touch interaction
  - Tap to place model
  - Drag to move
  - Pinch to scale (optional for MVP)
```

**Deliverables:**
- 3D laptop component visible in AR
- Can place on table/floor
- Model stays in place with proper tracking

**Key Code:**
```kotlin
// ArRenderer.kt
class ArRenderer(context: Context) {
    private val engine = Engine.create(context)
    private val renderer = Renderer(engine)
    private val scene = Scene.Builder().build()
    
    fun loadModel(modelFile: File): AssetInstance {
        val loader = gltfio.AssetLoader(engine, MaterialProvider(engine), null)
        return loader.createAsset(modelFile.absolutePath).first()
    }
    
    fun render(frame: Frame) {
        val view = View(engine).apply {
            scene = this@ArRenderer.scene
            camera = frame.camera
        }
        renderer.render(view)
    }
}

// ModelPlacementManager.kt
class ModelPlacementManager {
    fun placeModel(model: AssetInstance, hitTestResult: HitTestResult) {
        val anchor = hitTestResult.createAnchor()
        val pose = anchor.pose
        
        // Apply pose to model
        model.transform = floatArrayOf(
            pose.tx(), pose.ty(), pose.tz(),
            pose.qx(), pose.qy(), pose.qz(), pose.qw()
        )
    }
}
```

---

### Phase 6: Component Selection & UI Integration (Week 5-6)

**Goals:**
- Select which component to view
- Load appropriate models
- Display in AR

**Tasks:**
```
✓ Populate Firestore with component data
  - Component name, description, specs
  - Model file references
✓ Create ComponentViewModel with data loading
✓ Build component selection UI
  - List/grid of components
  - Thumbnails
✓ Wire up selection → AR view
  - Select component from list
  - Load model into AR
  - Display specs in overlay
✓ Create info overlay
  - Component name, specs
  - Assembly notes (optional for MVP)
```

**Deliverables:**
- Component list populated from Firebase
- Select component → see in AR
- Basic specs displayed

---

### Phase 7: Polish & Testing (Week 6-8)

**Goals:**
- Performance optimization
- Error handling
- Testing
- Documentation

**Tasks:**
```
✓ Performance profiling
  - Monitor frame rate
  - Optimize model loading
  - Reduce memory usage
✓ Error handling
  - No camera permission
  - No ARCore support
  - Model download failures
  - Network errors
✓ Unit tests
  - ViewModels
  - Repositories
  - Utilities
✓ Integration tests
  - Fragment navigation
  - Data loading
✓ Manual testing
  - Multiple devices
  - Different lighting
  - Different planes
✓ Documentation
  - README setup instructions
  - Code comments
  - Architecture diagram
```

**Deliverables:**
- Stable, tested app
- Handles errors gracefully
- Performs well on target devices

---

## PART 7: SPECIFIC IMPLEMENTATION DETAILS

### Firebase Setup

**Firestore Structure:**
```
components/
├── {componentId}/
│   ├── name: "NVIDIA RTX 4050"
│   ├── type: "GPU"
│   ├── category: "graphics"
│   ├── description: "Mobile GPU for gaming/creative work"
│   ├── specs: {
│   │   vram: "6GB GDDR6",
│   │   power: "70W",
│   │   bandwidth: "192-bit"
│   │ }
│   ├── modelUrl: "gs://bucket/models/gpu/rtx-4050.glb"
│   ├── modelSize: 5242880  // bytes
│   ├── thumbnailUrl: "gs://bucket/thumbnails/rtx-4050.jpg"
│   └── compatibility: ["Dell XPS 15", "Lenovo Legion", ...]

models/
├── {modelId}/
│   ├── fileName: "rtx-4050.glb"
│   ├── size: 5242880
│   ├── polyCount: 45000
│   ├── textureCount: 12
│   └── uploadedAt: timestamp
```

**Cloud Storage:**
```
gs://your-bucket/
├── models/
│   ├── gpu/
│   │   ├── nvidia-rtx-4050.glb
│   │   └── amd-radeon-7700m.glb
│   ├── ram/
│   │   ├── corsair-ddr5-32gb.glb
│   │   └── kingston-ddr5-16gb.glb
│   └── cpu/
│       ├── intel-i9-13900h.glb
│       └── amd-ryzen-9-7945hx.glb
├── thumbnails/
│   ├── nvidia-rtx-4050.jpg
│   └── ...
└── textures/
    ├── pbr/
    │   ├── roughness.hdr
    │   └── metallic.hdr
    └── ...
```

### Key Classes & Interfaces

```kotlin
// Domain Models
data class Component(
    val id: String,
    val name: String,
    val type: ComponentType,
    val description: String,
    val specifications: Map<String, String>,
    val modelUrl: String,
    val thumbnailUrl: String
)

enum class ComponentType {
    GPU, CPU, RAM, SSD, MOTHERBOARD, COOLING, STORAGE, POWER_SUPPLY
}

data class ARModel(
    val id: String,
    val filePath: String,
    val polyCount: Int,
    val textureCount: Int
)

// ViewModels
@HiltViewModel
class ArViewModel @Inject constructor(
    private val componentRepository: ComponentRepository,
    private val modelRepository: ModelRepository
) : ViewModel() {
    
    private val _selectedComponent = MutableStateFlow<Component?>(null)
    val selectedComponent: StateFlow<Component?> = _selectedComponent.asStateFlow()
    
    private val _arModels = MutableStateFlow<List<AssetInstance>>(emptyList())
    val arModels: StateFlow<List<AssetInstance>> = _arModels.asStateFlow()
    
    fun selectComponent(componentId: String) {
        viewModelScope.launch {
            val component = componentRepository.getComponent(componentId)
            _selectedComponent.value = component
            loadModel(component.modelUrl)
        }
    }
    
    private fun loadModel(modelUrl: String) {
        viewModelScope.launch {
            try {
                val modelFile = modelRepository.getModel(modelUrl)
                // Load into AR
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

// Repository Pattern
interface ComponentRepository {
    suspend fun getComponents(): List<Component>
    suspend fun getComponent(id: String): Component
    suspend fun searchComponents(query: String): List<Component>
}

class ComponentRepositoryImpl @Inject constructor(
    private val firestoreService: FirestoreService,
    private val componentDao: ComponentDao
) : ComponentRepository {
    
    override suspend fun getComponents(): List<Component> {
        return try {
            // Try remote first
            val remote = firestoreService.getComponents()
            componentDao.insertAll(remote)
            remote
        } catch (e: Exception) {
            // Fall back to local cache
            componentDao.getAll()
        }
    }
}
```

---

## PART 8: GRADLE CONFIGURATION (build.gradle.kts)

```kotlin
// Project-level build.gradle.kts
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

// App-level build.gradle.kts
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.laptopar"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-alpha"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // (use the complete list from Part 4)
}
```

---

## PART 9: RUNNING & TESTING ON DEVICE

### Connect Physical Device

```bash
# Enable Developer Mode on Android phone
# Settings → About Phone → Tap Build Number 7 times

# Enable USB Debugging
# Settings → Developer Options → USB Debugging

# Connect via USB cable

# Verify connection
adb devices
# Output: emulator-5554  device

# Install app
./gradlew installDebug

# Run app
./gradlew :app:run

# View logs
adb logcat | grep "LaptopAR"
```

### Build & Run
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test
./gradlew connectedAndroidTest
```

---

## PART 10: TIMELINE SUMMARY

```
Week 1:   Setup + Basic UI
          ├─ Android Studio project
          ├─ Firebase integration
          └─ Fragment navigation ready

Week 2:   Navigation + 3D Assets
          ├─ Component list UI
          ├─ Detail view UI
          └─ 3D models optimized & uploaded

Week 3-4: ARCore + Rendering
          ├─ Plane detection working
          ├─ Camera preview with AR
          └─ Filament rendering pipeline

Week 5-6: Component Selection & AR Integration
          ├─ Select component → load model
          ├─ Display in AR view
          └─ Component info overlay

Week 6-8: Polish + Testing
          ├─ Performance optimization
          ├─ Error handling
          ├─ Unit & integration tests
          └─ Device testing complete

PROTOTYPE READY ✓
```

---

## PART 11: RESOURCES & LEARNING

### Official Documentation
- **ARCore:** https://developers.google.com/ar
- **Android Studio:** https://developer.android.com/studio
- **Jetpack:** https://developer.android.com/jetpack
- **Filament:** https://google.github.io/filament/

### Model Sources
- **Sketchfab:** https://sketchfab.com (free models with CC licenses)
- **GrabCAD:** https://grabcad.com (technical parts)
- **TurboSquid:** https://www.turbosquid.com (premium models)

### 3D Modeling
- **Blender:** https://www.blender.org (free, open-source)
- **Fusion 360:** https://www.autodesk.com/products/fusion-360 (free educational license)
- **Model Viewer:** https://modelviewer.dev (test glTF models in browser)

### Code Examples
- **ARCore Samples:** https://github.com/google-ar/arcore-android-sdk
- **Filament Samples:** https://github.com/google/filament/tree/main/samples
- **Android Architecture Samples:** https://github.com/android/architecture-samples

---

## PART 12: QUICK CHECKLIST FOR STARTING

```
BEFORE YOU CODE:

[ ] Install Android Studio (latest)
[ ] Install JDK 17
[ ] Create Firebase project
    [ ] Enable Firestore
    [ ] Enable Cloud Storage
    [ ] Download google-services.json
[ ] Have a physical Android device with ARCore support
[ ] Enable USB debugging on device
[ ] Download 3-5 laptop component models from Sketchfab
[ ] Test models in ModelViewer.dev before using

READY TO START DEVELOPMENT ✓
```

---

## Summary

This is a comprehensive native Android development path for your AR laptop assembly app. The stack is:

**Language:** Kotlin  
**AR Framework:** ARCore + Filament  
**Architecture:** MVVM + Repository Pattern  
**Backend:** Firebase (Firestore + Cloud Storage)  
**Camera:** CameraX  
**Async:** Coroutines + Flow  
**DI:** Hilt  

**Timeline:** 1-2 months for prototype, 10 months for full thesis project

**Next Steps:**
1. Download Android Studio
2. Create Firebase project
3. Set up your first Android project
4. Follow the 7-phase development plan

Good luck with your project!
