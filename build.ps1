if (Test-Path out) { Remove-Item -Recurse -Force out }
New-Item -ItemType Directory -Path out | Out-Null

$JavaFiles = Get-ChildItem -Path "src/main/java" -Filter *.java -Recurse | Select-Object -ExpandProperty FullName

javac --release 25 -d out $JavaFiles

if ($LASTEXITCODE -eq 0) {
    java --module-path out --module com.github.jamesrvickers.drawingidentifier/com.github.jamesrvickers.drawingidentifier.Main
    jar --create --file=out/drawingidentifier-local.jar -C out .
}