# PlugCLI Local Installer for Windows
$ErrorActionPreference = "Stop"

$installDir = "$env:USERPROFILE\.plugcli"
# Download directly from Maven Central!
$mavenUrl = "https://repo1.maven.org/maven2/io/github/akshita142/plugcli-core/1.2.1/plugcli-core-1.2.1.jar"
$localJarPath = "$installDir\plugcli.jar"

Write-Host "Installing PlugCLI globally..." -ForegroundColor Cyan

# 1. Create install directory
if (-Not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Path $installDir -Force | Out-Null
}

# 2. Download the JAR from the internet
Write-Host "Downloading framework from Maven Central..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $mavenUrl -OutFile $localJarPath
} catch {
    Write-Host "Error: Failed to download the framework. Check your internet connection." -ForegroundColor Red
    return
}

# 4. Create a batch launcher so user can type `plugcli` instead of `java -jar`
$batchContent = "@echo off`njava -jar `"$installDir\plugcli.jar`" %*"
Set-Content -Path "$installDir\plugcli.bat" -Value $batchContent

# 5. Add to Windows PATH
$currentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($currentPath -notlike "*$installDir*") {
    [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$installDir", "User")
    Write-Host "Added PlugCLI to your PATH." -ForegroundColor Green
}

Write-Host "`n========================================" -ForegroundColor Green
Write-Host "  PlugCLI installed successfully!" -ForegroundColor Green
Write-Host "  Restart your terminal, then run:" -ForegroundColor Green
Write-Host "    plugcli help" -ForegroundColor Yellow
Write-Host "    plugcli init my-plugin" -ForegroundColor Yellow
Write-Host "========================================`n" -ForegroundColor Green
