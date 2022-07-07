Environment setup for development
---

1. Install the following tools:
    - [Android Studio](https://developer.android.com/studio)
	- [git](https://git-scm.com)
	- [git-lfs](https://git-lfs.github.com)

2. Open _Android Studio_, go to the _SDK Manager_, click on _Show Package Details_ and install the following packages for the target version you'll be developing for (*):
    - Android SDK Platform
	- Sources for Android
	- Google Play Intel x86 Atom_64 System Image
	
	(*) Open the `versions.gradle` file on this project and check the value for `target_sdk` (on ext.android_build).

    Make sure you also have the following packages installed:
    - Android Emulator
	- Intel x86 Emulator Accelerator (HAXM Installer)


3. Set the following variables in your global `gradle.properties` file (*):
    ```
    useDependenciesProxy
    ```
    - `useDependenciesProxy` indicates that you want to use `http://nexus.genexus.com` as a proxy for downloading Maven artifacts

    (*) If you don't have one already, create the file. It should be located in `~/.gradle` for Mac OS or Linux users, and in `%UserProfile%/.gradle` for Windows users. 

4. Create a `Workspace/settings.gradle` file. The `Workspace` directory
 is ignored by git. You may use this directory to import app projects in
 order to test your changes. For example:
    ```
    include ':AppMain'
    project(':AppMain').projectDir = new File('Workspace/AppMain')
    ```
    
    **Note** that you'll have to modify the `build.gradle` of your generated
     project, as follows:
     
     - Remove the `signingConfigs` and `buildTypes` sections
     - Change the `artifact` dependencies for `project` dependencies
     - Add `deps.androidx.app_compat` as an `implementation` dependency

5. _Optional:_ Configure your git client to run the pre-push hook we have. This runs
the static code analyzers before you push your changes, thus avoiding CI
 build errors due to bad formatting, etc.

    ```
    git config core.hooksPath .githooks
    ```
