# Autore: Andrea Delmastro
# Script powershell per automatizzare il processo di esecuzione
$Script_name = $MyInvocation.MyCommand.Name
$defautl_lft_path = ".\docs\test\translator\"
if($args[0] -eq "--help") {
    Write-Host -ForegroundColor blue "$Script_name Jasmin_path source_lft_path [abs=true|false, compile=true|false]`n"
    Write-Host "Lo script automatizza il processo di compliazione ed esecuzione del programma source_lft_path scritto secondo le specifiche del linguaggio lft.`nIl parametro abs e' facoltativo e viene utilizzato per specificare se il source_lft_path e' assoluto oppure no. In caso abs=false il file viene prelevato da docs/test/translator.`nIl parametro comiple specifica se le classi devono essere ricompilate.`nEsempio di esecuzione:`n.\run.ps1 `"C:\Users\Andrea\Documents\unito\lft\Lab\jasmin-2.4`" `"bool.lft`" false false"
} elseif($args.Count -ne 2 -and $args.Count -ne 4) {
    Write-Output "[Error] -> $Script_name Jasmin_path source_lft_path [abs=true|false, compile=true|false]"
} else {
    $Jasmin_path = $args[0]
    $source_lft_path = $args[1]
    if($args[2] -ne $true) { $source_lft_path = "$defautl_lft_path$source_lft_path" }
    if($args[3] -eq $true) { javac -d .\out\production\Es05_01\ .\src\*.java }
    if($?) { java -classpath .\out\production\Es05_01\ Translator $source_lft_path }
    if($?) { java -jar $Jasmin_path\jasmin.jar -d ./out/Jasmin ./out/Jasmin/Output.j }
    if($?) { java -classpath .\out\Jasmin\ Output }
}