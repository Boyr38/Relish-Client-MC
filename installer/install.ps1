param(
	[Parameter(Mandatory = $true)][string]$ModJar,
	[string]$McVersion = "26.2"
)

$ErrorActionPreference = "Stop"
$ProgressPreference = "SilentlyContinue"
$headers = @{ "User-Agent" = "RelishClientInstaller/2.1" }

try {
	$mc = Join-Path $env:APPDATA ".minecraft"
	if (-not (Test-Path $mc)) {
		throw "Could not find $mc. Install and run the official Minecraft Launcher once first."
	}

	# 1. Find a Java to run the Fabric installer (the launcher ships its own).
	Write-Host "[1/4] Looking for Java..."
	$java = $null
	$runtime = Join-Path $mc "runtime"
	if (Test-Path $runtime) {
		$java = Get-ChildItem $runtime -Recurse -Filter java.exe -ErrorAction SilentlyContinue |
			Select-Object -First 1 -ExpandProperty FullName
	}
	if (-not $java) {
		$cmd = Get-Command java -ErrorAction SilentlyContinue
		if ($cmd) { $java = $cmd.Source }
	}
	if (-not $java) {
		throw "No Java found. Launch Minecraft $McVersion once from the official launcher (it downloads Java), then run this installer again."
	}

	# 2. Install Fabric Loader.
	Write-Host "[2/4] Installing Fabric Loader for Minecraft $McVersion..."
	$installers = Invoke-RestMethod "https://meta.fabricmc.net/v2/versions/installer" -Headers $headers
	$inst = $installers | Where-Object { $_.stable } | Select-Object -First 1
	$installerJar = Join-Path $env:TEMP "fabric-installer-$($inst.version).jar"
	Invoke-WebRequest $inst.url -OutFile $installerJar -Headers $headers
	& $java -jar $installerJar client -mcversion $McVersion -dir $mc
	if ($LASTEXITCODE -ne 0) { throw "Fabric installer failed (exit code $LASTEXITCODE)." }

	# 3. Fabric API.
	Write-Host "[3/4] Getting Fabric API..."
	$mods = Join-Path $mc "mods"
	New-Item -ItemType Directory -Force -Path $mods | Out-Null
	$q = "https://api.modrinth.com/v2/project/fabric-api/version?game_versions=%5B%22$McVersion%22%5D&loaders=%5B%22fabric%22%5D"
	$versions = Invoke-RestMethod $q -Headers $headers
	if (-not $versions -or $versions.Count -eq 0) { throw "No Fabric API build found for Minecraft $McVersion." }
	$file = $versions[0].files | Where-Object { $_.primary } | Select-Object -First 1
	if (-not $file) { $file = $versions[0].files[0] }
	$dest = Join-Path $mods $file.filename
	if (-not (Test-Path $dest)) {
		Invoke-WebRequest $file.url -OutFile $dest -Headers $headers
	}

	# 4. Relish Client.
	Write-Host "[4/4] Installing Relish Client..."
	Get-ChildItem $mods -Filter "relishclient-*.jar" -ErrorAction SilentlyContinue | Remove-Item -Force
	$jarName = "relishclient.jar"
	Copy-Item $ModJar (Join-Path $mods $jarName) -Force

	Write-Host ""
	Write-Host "Done! Open the Minecraft Launcher, choose the 'fabric-loader-$McVersion' profile and press Play."
	Write-Host "In game: Right Shift opens the Relish menu."
}
catch {
	Write-Host ""
	Write-Host "Install failed: $($_.Exception.Message)" -ForegroundColor Red
}
Read-Host "Press Enter to close"
