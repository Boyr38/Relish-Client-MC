$mods = Join-Path $env:APPDATA ".minecraft\mods"
Remove-Item (Join-Path $mods "relishclient*.jar") -Force -ErrorAction SilentlyContinue
