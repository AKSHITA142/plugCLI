# Clear the plugin out folder to prevent stale classes
if (Test-Path "out_plugin") {
    Remove-Item -Recurse -Force "out_plugin"
}
mkdir "out_plugin" -Force | Out-Null

# Compile all Java files inside src/myPlugin
Write-Host "Compiling plugin code..."
$javaFiles = Get-ChildItem -Recurse -Filter *.java src\myPlugin | Select-Object -ExpandProperty FullName

if ($javaFiles) {
    javac -cp out -d out_plugin $javaFiles
    
    # Package into a JAR
    Write-Host "Creating JAR file..."
    if (-Not (Test-Path "plugins")) {
        mkdir "plugins" -Force | Out-Null
    }
    
    # Using the official JDK jar tool
    & "C:\Program Files\Java\jdk-21\bin\jar.exe" cvf plugins/testplugin.jar -C out_plugin .
    
    Write-Host "`nSuccessfully built plugins/testplugin.jar!" -ForegroundColor Green
} else {
    Write-Host "No Java files found in src\myPlugin" -ForegroundColor Red
}
