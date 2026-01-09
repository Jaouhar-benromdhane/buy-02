# Configuration automatique Jenkins pour buy-02
# Token SonarQube: sqa_9db7781421cc639f1c5e28affb4058b8c3fb8d90

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host " CONFIGURATION JENKINS AUTOMATIQUE" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

$jenkinsUrl = "http://localhost:9090"
$sonarToken = "sqa_9db7781421cc639f1c5e28affb4058b8c3fb8d90"

Write-Host "Token SonarQube enregistre: $sonarToken" -ForegroundColor Green

Write-Host "`n============================================" -ForegroundColor Yellow
Write-Host " ETAPES MANUELLES JENKINS" -ForegroundColor Yellow
Write-Host "============================================`n" -ForegroundColor Yellow

Write-Host "ETAPE 1: Installer le plugin SonarQube Scanner" -ForegroundColor Cyan
Write-Host "  1. Ouvrir http://localhost:9090" -ForegroundColor White
Write-Host "  2. Manage Jenkins -> Plugins" -ForegroundColor White
Write-Host "  3. Available plugins -> Rechercher 'SonarQube Scanner'" -ForegroundColor White
Write-Host "  4. Cocher et cliquer 'Install'" -ForegroundColor White
Write-Host "  5. Attendre installation (2 min)" -ForegroundColor White

Write-Host "`nETAPE 2: Configurer SonarQube Server" -ForegroundColor Cyan
Write-Host "  1. Manage Jenkins -> System" -ForegroundColor White
Write-Host "  2. Scroller vers 'SonarQube servers'" -ForegroundColor White
Write-Host "  3. Cliquer 'Add SonarQube'" -ForegroundColor White
Write-Host "     Name: SonarQube" -ForegroundColor Yellow
Write-Host "     Server URL: http://sonarqube:9000" -ForegroundColor Yellow
Write-Host "  4. Server authentication token:" -ForegroundColor White
Write-Host "     Cliquer 'Add' -> 'Jenkins'" -ForegroundColor Yellow
Write-Host "     Kind: Secret text" -ForegroundColor Yellow
Write-Host "     Secret: $sonarToken" -ForegroundColor Green
Write-Host "     ID: sonarqube-token" -ForegroundColor Yellow
Write-Host "     Description: SonarQube Token" -ForegroundColor Yellow
Write-Host "  5. Sauvegarder" -ForegroundColor White

Write-Host "`nETAPE 3: Configurer SonarQube Scanner" -ForegroundColor Cyan
Write-Host "  1. Manage Jenkins -> Tools" -ForegroundColor White
Write-Host "  2. Scroller vers 'SonarQube Scanner installations'" -ForegroundColor White
Write-Host "  3. Cliquer 'Add SonarQube Scanner'" -ForegroundColor White
Write-Host "     Name: SonarScanner" -ForegroundColor Yellow
Write-Host "     Cocher 'Install automatically'" -ForegroundColor Yellow
Write-Host "  4. Sauvegarder" -ForegroundColor White

Write-Host "`nETAPE 4: Creer le Pipeline Job" -ForegroundColor Cyan
Write-Host "  1. Dashboard Jenkins -> New Item" -ForegroundColor White
Write-Host "  2. Name: buy-02-pipeline" -ForegroundColor Yellow
Write-Host "  3. Type: Pipeline" -ForegroundColor Yellow
Write-Host "  4. Cliquer OK" -ForegroundColor White
Write-Host "  5. Dans la configuration:" -ForegroundColor White
Write-Host "     Pipeline -> Definition: Pipeline script from SCM" -ForegroundColor Yellow
Write-Host "     SCM: Git" -ForegroundColor Yellow
Write-Host "     Repository URL: file:///workspace" -ForegroundColor Yellow
Write-Host "     Branch Specifier: */main" -ForegroundColor Yellow
Write-Host "     Script Path: Jenkinsfile" -ForegroundColor Yellow
Write-Host "  6. Sauvegarder" -ForegroundColor White

Write-Host "`nETAPE 5: EXECUTER le Pipeline" -ForegroundColor Cyan
Write-Host "  1. Aller sur buy-02-pipeline" -ForegroundColor White
Write-Host "  2. Cliquer 'Build Now'" -ForegroundColor Yellow
Write-Host "  3. Voir les logs en temps reel" -ForegroundColor White
Write-Host "  4. Attendre fin execution" -ForegroundColor White
Write-Host "  5. Verifier SonarQube results" -ForegroundColor White

Write-Host "`n============================================" -ForegroundColor Green
Write-Host " TOKEN SONARQUBE SAUVEGARDE" -ForegroundColor Green
Write-Host "============================================`n" -ForegroundColor Green

Write-Host "Token: $sonarToken" -ForegroundColor Yellow
Write-Host "`nCe token sera utilise dans Jenkins pour s'authentifier sur SonarQube.`n" -ForegroundColor White

# Sauvegarder le token dans un fichier
$sonarToken | Out-File -FilePath "sonarqube-token.txt" -Encoding UTF8
Write-Host "Token sauvegarde dans: sonarqube-token.txt" -ForegroundColor Green

# Ouvrir Jenkins
Write-Host "`nOuverture de Jenkins..." -ForegroundColor Yellow
Start-Process $jenkinsUrl

Write-Host "`nSuis les etapes ci-dessus pour finaliser la config !`n" -ForegroundColor Cyan
