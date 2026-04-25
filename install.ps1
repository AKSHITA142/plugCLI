# PlugCLI Local Installer for Windows
$ErrorActionPreference = "Stop"

$installDir = "$env:USERPROFILE\.plugcli"
# We will use the JAR that Maven builds locally!
$localJarPath = ".\target\plugcli-core-1.0.0.jar"

Write-Host "Installing PlugCLI locally..." -ForegroundColor Cyan

# 1. Check if the JAR actually exists
if (-Not (Test-Path $localJarPath)) {
    Write-Host "Error: Cannot find $localJarPath!" -ForegroundColor Red
    Write-Host "Please run 'mvn clean package' first to build the fat JAR." -ForegroundColor Yellow
    exit
}

# 2. Create install directory
if (-Not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Path $installDir -Force | Out-Null
}

# 3. Copy the local JAR to the install folder
Write-Host "Copying framework to $installDir..."
Copy-Item -Path $localJarPath -Destination "$installDir\plugcli.jar" -Force

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
