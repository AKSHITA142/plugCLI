# Local PlugCLI Installer (For Development & Testing)
$ErrorActionPreference = "Stop"

$installDir = "$env:USERPROFILE\.plugcli"
$localJarPath = ".\target\plugcli-core-1.1.0.jar"

Write-Host "Installing LATEST PlugCLI locally from your code..." -ForegroundColor Cyan

# 1. Check if the JAR exists
if (-Not (Test-Path $localJarPath)) {
    Write-Host "Error: Cannot find $localJarPath!" -ForegroundColor Red
    Write-Host "Please run 'mvn clean package' first to build your latest code." -ForegroundColor Yellow
    exit
}

# 2. Create install directory
if (-Not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Path $installDir -Force | Out-Null
}

# 3. Copy the local JAR to the install folder (OVERWRITING the old internet one)
Write-Host "Copying your LATEST framework to $installDir..."
Copy-Item -Path $localJarPath -Destination "$installDir\plugcli.jar" -Force

# 4. Create batch launcher
$batchContent = "@echo off`njava -jar `"$installDir\plugcli.jar`" %*"
Set-Content -Path "$installDir\plugcli.bat" -Value $batchContent

# 5. Add to PATH
$currentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($currentPath -notlike "*$installDir*") {
    [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$installDir", "User")
}

Write-Host "`nLATEST Framework installed successfully! You can now test 'init' and your new commands." -ForegroundColor Green
