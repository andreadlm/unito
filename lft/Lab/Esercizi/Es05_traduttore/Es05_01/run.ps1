# Autore: Andrea Delmastro
# Script per automatizzare il processo di esecuzione
$Script_name = $MyInvocation.MyCommand.Name
$defautl_lft_path = ".\docs\test\translator\"
if($args[0] -eq "--help") {
    Write-Host -ForegroundColor blue "$Script_name Jasmin_path source_lft_path [abs=true|false, compile=true|false]`n"
    Write-Host "Lo script automatizza il processo di compliazione ed esecuzione del programma source_lft_path scritto secondo le specifiche del linguaggio lft.`nIl parametro abs e' facoltativo e viene utilizzato per specificare se il source_lft_path e' assoluto oppure no. In caso abs=false il file viene prelevato da docs/test/translator.`nIl parametro comiple specifica se le classi devono essere ricompilate."
} elseif($args.Count -ne 2 -and $args.Count -ne 4) {
    Write-Output "[Error] -> $Script_name Jasmin_path source_lft_path [abs=true|false, compile=true|false]"
} else {
    $Jasmin_path = $args[0]
    $source_lft_path = $args[1]
    if($args[2] -ne $true) { $source_lft_path = "$defautl_lft_path$source_lft_path" }
    if($args[3] -eq $true) { javac -d .\out\production\Es05_01\ .\src\*.java }
    java -classpath .\out\production\Es05_01\ Translator $source_lft_path
    java -jar $Jasmin_path\jasmin.jar -d ./out/Jasmin ./out/Jasmin/Output.j
    java -classpath .\out\Jasmin\ Output
}