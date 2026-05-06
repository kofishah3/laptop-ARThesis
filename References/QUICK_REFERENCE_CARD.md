# Quick Reference Card
## AR Laptop Assembly App - Development Setup

---

## FASTEST START (Do This First)

```
1. Install Android Studio
2. Connect Xiaomi via USB
3. Enable USB Debugging on phone
4. Create new Android project (API 24, Kotlin)
5. Click Run → Select device
6. SUCCESS: App appears on phone
```

**Total Time: 1 hour**

---

## YOUR DEVELOPMENT ENVIRONMENT

```
IDE:              Android Studio (latest)
Device:           Xiaomi Redmi Note 14 Pro (UI only)
Target Device:    Samsung A32 5G (for AR later)
Language:         Kotlin
Min API:          24 (Android 7.0)
Backend:          Firebase (Firestore + Cloud Storage)
```

---

## REQUIRED INSTALLATIONS

| Software | Download From | Time |
|----------|---------------|------|
| Android Studio | developer.android.com/studio | 30 min |
| JDK 17+ | Included with Android Studio | Auto |
| Git | (optional) | - |
| Blender | blender.org | (skip for now) |

---

## FOLDER STRUCTURE (Copy This)

```
com.example.laptopar/
├── di/                (Hilt dependency injection)
├── ui/
│   ├── ar/            (AR view - empty for now)
│   ├── components/    (Component list screen)
│   ├── detail/        (Detail screen)
│   └── common/        (Shared utilities)
├── data/
│   ├── remote/        (Firebase services)
│   ├── local/         (Room + SharedPreferences)
│   └── repository/    (Repository layer)
├── domain/
│   ├── model/         (Data classes)
│   └── usecase/       (Business logic)
└── util/              (Helpers)
```

---

## CRITICAL GRADLE DEPENDENCIES

```kotlin
// Firebase
implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
implementation("com.google.firebase:firebase-firestore-ktx")
implementation("com.google.firebase:firebase-storage-ktx")

// Hilt (Dependency Injection)
implementation("com.google.dagger:hilt-android:2.48")
kapt("com.google.dagger:hilt-compiler:2.48")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
```

---

## BUILD & RUN COMMANDS

```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# View logs
adb logcat | grep "LaptopAR"

# Install on device
./gradlew installDebug

# Check connected devices
adb devices
```

---

## KEY ANNOTATIONS TO REMEMBER

```kotlin
// Mark app entry point
@AndroidEntryPoint
class MainActivity : AppCompatActivity() { }

// Mark fragments
@AndroidEntryPoint
class MyFragment : Fragment() { }

// Mark ViewModels with Hilt
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() { }

// Mark your App class
@HiltAndroidApp
class LaptopArApplication : Application()
```

---

## ESSENTIAL CLASS TEMPLATES

### ViewModel Template
```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // Fetch data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
```

### Fragment Template
```kotlin
@AndroidEntryPoint
class MyFragment : Fragment() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                // Update UI
            }
        }
    }
}
```

### Repository Template
```kotlin
class MyRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: MyDao
) {
    suspend fun getData(): List<Item> {
        return try {
            firestore.collection("items").get().await()
                .toObjects(Item::class.java)
        } catch (e: Exception) {
            dao.getAll()  // Fallback to local
        }
    }
}
```

---

## DEBUGGING CHECKLIST

**App Won't Install:**
- [ ] Device connected via USB?
- [ ] USB Debugging enabled?
- [ ] Device appears in `adb devices`?
- [ ] Try different USB port?

**App Crashes on Startup:**
- [ ] Check Logcat for red error messages
- [ ] Missing layout file?
- [ ] Wrong package name in manifest?
- [ ] Typo in activity/fragment name?

**Gradle Sync Fails:**
- [ ] File → Invalidate Caches → Restart
- [ ] Delete .gradle folder in project
- [ ] Check internet connection
- [ ] Check JDK is JDK 17+

**UI Not Appearing:**
- [ ] Is layout file in res/layout/?
- [ ] Check resource IDs match layout names
- [ ] Did you call `setContentView()`?

---

## WEEK 1 MILESTONES

- [ ] **Day 1:** Android Studio + device setup (1 hour)
- [ ] **Day 2:** First app runs on phone (1 hour)
- [ ] **Day 3:** Firebase connected + basic structure (1 hour)
- [ ] **Days 4-5:** Three UI screens built + mock data (2 hours)
- [ ] **Day 6:** Clean up, test, fix bugs (1-2 hours)
- [ ] **Day 7:** Connect to real Firestore data (1-2 hours)

---

## COMMON ERRORS & FIXES

| Error | Cause | Fix |
|-------|-------|-----|
| `Cannot find symbol: class MainActivity` | Activity not in manifest | Add to AndroidManifest.xml |
| `Unresolved reference: R.layout.activity_main` | Missing layout file | Create res/layout/activity_main.xml |
| `Execution failed for task ':app:compileDebugKotlin'` | Gradle sync not done | Click "Sync Now" |
| `No connected devices` | USB not recognized | Check USB cable, enable debugging |
| `AAPT: error: resource android:attr/selectableItemBackground not found` | Old Material version | Update `com.google.android.material:material` |

---

## IMPORTANT FILES LOCATIONS

```
Your Project/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml      ← App permissions
│   │   ├── java/com/example/laptopar/  ← Your code
│   │   └── res/
│   │       ├── layout/              ← XML UI files
│   │       ├── values/              ← Strings, colors, dimens
│   │       └── drawable/            ← Images, icons
│   ├── build.gradle.kts             ← Dependencies & build config
│   └── google-services.json         ← Firebase config (download from console)
└── build.gradle.kts                 ← Project-level config
```

---

## GIT SETUP (Optional but Recommended)

```bash
# Initialize git
git init

# Create .gitignore for Android
echo "*.iml
.gradle/
.idea/
build/
.DS_Store" > .gitignore

# First commit
git add .
git commit -m "Initial Android project setup"
```

---

## USEFUL KEYBOARD SHORTCUTS

| Action | Shortcut |
|--------|----------|
| Run app | Shift + F10 |
| Debug app | Shift + F9 |
| Build project | Ctrl + F9 |
| Open logcat | Alt + 6 |
| Open device manager | Shift + Ctrl + Q |
| Open project structure | Ctrl + Alt + Shift + S |
| Format code | Ctrl + Alt + L |
| Optimize imports | Ctrl + Alt + O |

---

## FIREBASE SETUP QUICK STEPS

```
1. Go to console.firebase.google.com
2. Click "Create Project" → Name it "LaptopAR"
3. When created, click "Add App" → Select Android
4. Enter package: com.example.laptopar
5. Download google-services.json
6. Drag it into app/ folder in Android Studio
7. Add Firebase dependencies (see above)
8. Sync Gradle
9. Done!
```

---

## NEXT ACTION

Open this file alongside your development:
→ **START_BUILDING_NOW.md** ← Start with Phase 0, Step 1

Follow each step in order. Don't skip ahead.

**Estimated completion: 4-5 hours for a working UI**

---

Last Updated: 2026-05-04
For full context, see: Android_AR_Development_Roadmap.md
