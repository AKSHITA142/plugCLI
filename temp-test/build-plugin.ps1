# PlugCLI Plugin Build Script
# Compiles your plugin and creates a JAR in the plugins/ folder.

if (Test-Path "out_plugin") { Remove-Item -Recurse -Force "out_plugin" }
mkdir "out_plugin" -Force | Out-Null

# Use the local framework JAR as classpath
$frameworkJar = "plugcli-core-1.2.2.jar"
if (-Not (Test-Path $frameworkJar)) {
    Write-Host "Framework JAR missing. Copying from global installation..." -ForegroundColor Yellow
    $globalJar = "$env:USERPROFILE\.plugcli\plugcli.jar"
    if (Test-Path $globalJar) {
        Copy-Item $globalJar .\$frameworkJar -Force
        Write-Host "Copied successfully." -ForegroundColor Green
    } else {
        Write-Host "ERROR: $frameworkJar not found globally either!" -ForegroundColor Red
        exit 1
    }
}

Write-Host "Compiling plugin code..."
$javaFiles = Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName

if ($javaFiles) {
    javac -cp $frameworkJar -d out_plugin $javaFiles
    if ($LASTEXITCODE -ne 0) {
        Write-Host "`nCompilation FAILED!" -ForegroundColor Red
        exit 1
    }
    Write-Host "Creating JAR file..."
    if (-Not (Test-Path "plugins")) { mkdir "plugins" -Force | Out-Null }
    & "C:\Program Files\Java\jdk-21\bin\jar.exe" cvf plugins/myplugin.jar -C out_plugin .
    Write-Host "`nBuild successful!" -ForegroundColor Green
    Write-Host "Run: plugcli sample YourName" -ForegroundColor Cyan
} else {
    Write-Host "No Java files found in src/" -ForegroundColor Red
}
