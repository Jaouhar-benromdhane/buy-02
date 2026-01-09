# Script de configuration automatique Jenkins + SonarQube
# buy-02 E-Commerce Platform

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host " CONFIGURATION JENKINS + SONARQUBE" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

# URLs
$jenkinsUrl = "http://localhost:9090"
$sonarUrl = "http://localhost:9000"

Write-Host "Step 1: Verification Jenkins..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri $jenkinsUrl -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK] Jenkins accessible sur $jenkinsUrl" -ForegroundColor Green
} catch {
    Write-Host "[ERREUR] Jenkins non accessible. Attends 1 minute..." -ForegroundColor Red
    exit 1
}

Write-Host "`nStep 2: Verification SonarQube..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri $sonarUrl -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK] SonarQube accessible sur $sonarUrl" -ForegroundColor Green
} catch {
    Write-Host "[ERREUR] SonarQube non accessible. Attends 1 minute..." -ForegroundColor Red
    exit 1
}

Write-Host "`n============================================" -ForegroundColor Green
Write-Host " SERVICES OPERATIONNELS!" -ForegroundColor Green
Write-Host "============================================`n" -ForegroundColor Green

Write-Host "Jenkins:" -ForegroundColor Cyan
Write-Host "  URL: $jenkinsUrl" -ForegroundColor White
Write-Host "  User: admin" -ForegroundColor White
Write-Host "  Pass: admin (ou premier login sans password)" -ForegroundColor White

Write-Host "`nSonarQube:" -ForegroundColor Cyan
Write-Host "  URL: $sonarUrl" -ForegroundColor White
Write-Host "  User: admin" -ForegroundColor White
Write-Host "  Pass: admin (changer au premier login)" -ForegroundColor White

Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host " PROCHAINES ETAPES MANUELLES" -ForegroundColor Cyan
Write-Host "============================================`n" -ForegroundColor Cyan

Write-Host "1. SONARQUBE - Configuration du projet:" -ForegroundColor Yellow
Write-Host "   a. Ouvrir http://localhost:9000" -ForegroundColor White
Write-Host "   b. Login: admin / admin (changer password)" -ForegroundColor White
Write-Host "   c. Cliquer 'Create Project' -> 'Manually'" -ForegroundColor White
Write-Host "   d. Project key: buy-02" -ForegroundColor White
Write-Host "   e. Display name: buy-02-ecommerce" -ForegroundColor White
Write-Host "   f. Generer TOKEN (copier et sauvegarder)" -ForegroundColor White

Write-Host "`n2. JENKINS - Configuration:" -ForegroundColor Yellow
Write-Host "   a. Ouvrir http://localhost:9090" -ForegroundColor White
Write-Host "   b. Skip wizard si demande" -ForegroundColor White
Write-Host "   c. Manage Jenkins -> Plugins" -ForegroundColor White
Write-Host "      -> Installer: 'SonarQube Scanner'" -ForegroundColor White
Write-Host "   d. Manage Jenkins -> System" -ForegroundColor White
Write-Host "      -> SonarQube servers:" -ForegroundColor White
Write-Host "         Name: SonarQube" -ForegroundColor White
Write-Host "         Server URL: http://sonarqube:9000" -ForegroundColor White
Write-Host "         Token: [ton token SonarQube]" -ForegroundColor White
Write-Host "   e. Manage Jenkins -> Tools" -ForegroundColor White
Write-Host "      -> SonarQube Scanner:" -ForegroundColor White
Write-Host "         Name: SonarScanner" -ForegroundColor White
Write-Host "         Install automatically" -ForegroundColor White

Write-Host "`n3. JENKINS - Creer le pipeline:" -ForegroundColor Yellow
Write-Host "   a. New Item -> Pipeline" -ForegroundColor White
Write-Host "   b. Name: buy-02-pipeline" -ForegroundColor White
Write-Host "   c. Pipeline script from SCM" -ForegroundColor White
Write-Host "   d. SCM: Git" -ForegroundColor White
Write-Host "   e. Repository URL: E:/pZone01/Pjava/buy-02" -ForegroundColor White
Write-Host "   f. Branch: */main" -ForegroundColor White
Write-Host "   g. Script Path: Jenkinsfile" -ForegroundColor White
Write-Host "   h. SAVE" -ForegroundColor White

Write-Host "`n4. EXECUTER le pipeline:" -ForegroundColor Yellow
Write-Host "   a. Cliquer 'Build Now'" -ForegroundColor White
Write-Host "   b. Voir les resultats en temps reel" -ForegroundColor White
Write-Host "   c. Verifier SonarQube results" -ForegroundColor White

Write-Host "`n============================================" -ForegroundColor Green
Write-Host " TU ES PRET!" -ForegroundColor Green
Write-Host "============================================`n" -ForegroundColor Green

# Ouvrir les URLs dans le navigateur
Write-Host "Ouverture des URLs..." -ForegroundColor Yellow
Start-Process $jenkinsUrl
Start-Sleep -Seconds 2
Start-Process $sonarUrl

Write-Host "`nURLs ouvertes dans ton navigateur!" -ForegroundColor Green
Write-Host "Suis les etapes ci-dessus pour configurer.`n" -ForegroundColor White
