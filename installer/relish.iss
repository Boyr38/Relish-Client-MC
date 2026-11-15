; Builds RelishClient-Setup.exe. Needs installer\relishclient.jar next to this file (the CI stages it).
#ifndef AppVersion
  #define AppVersion "2.1.1"
#endif

[Setup]
AppId={{B3A4C3E2-5B53-4D0B-9E0A-6A1E5F7D2C11}
AppName=Relish Client
AppVersion={#AppVersion}
AppPublisher=Relish
DefaultDirName={localappdata}\Relish Client
DisableDirPage=yes
DisableProgramGroupPage=yes
PrivilegesRequired=lowest
OutputDir=output
OutputBaseFilename=RelishClient-Setup
SetupIconFile=relish.ico
UninstallDisplayIcon={app}\relish.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Files]
Source: "relishclient.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "install.ps1"; DestDir: "{app}"; Flags: ignoreversion
Source: "uninstall.ps1"; DestDir: "{app}"; Flags: ignoreversion
Source: "relish.ico"; DestDir: "{app}"; Flags: ignoreversion

[Run]
Filename: "powershell.exe"; Parameters: "-NoProfile -ExecutionPolicy Bypass -File ""{app}\install.ps1"" -ModJar ""{app}\relishclient.jar"""; StatusMsg: "Installing Fabric, Fabric API and Relish Client..."; Flags: waituntilterminated

[UninstallRun]
Filename: "powershell.exe"; Parameters: "-NoProfile -ExecutionPolicy Bypass -File ""{app}\uninstall.ps1"""; Flags: waituntilterminated runhidden
