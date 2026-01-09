# Script pour supprimer l'ancien job et créer le bon Pipeline
Write-Host "=== Suppression et création du Pipeline Jenkins ===" -ForegroundColor Cyan

$jenkinsUrl = "http://localhost:9090"
$jobName = "buy-02-pipeline"

# 1. Supprimer le job existant
Write-Host "`n[1/3] Suppression de l'ancien job (si existe)..." -ForegroundColor Yellow
try {
    $deleteUrl = "$jenkinsUrl/job/$jobName/doDelete"
    Invoke-WebRequest -Uri $deleteUrl -Method POST -UseBasicParsing | Out-Null
    Write-Host "✓ Ancien job supprimé" -ForegroundColor Green
    Start-Sleep -Seconds 2
} catch {
    Write-Host "⚠ Aucun job à supprimer (ou déjà supprimé)" -ForegroundColor Gray
}

# 2. Configuration XML du Pipeline
Write-Host "`n[2/3] Création du nouveau Pipeline..." -ForegroundColor Yellow
$pipelineConfig = @"
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@1400.v7fd111b_ec82f">
  <description>Pipeline CI/CD pour buy-02 ecommerce avec SonarQube</description>
  <keepDependencies>false</keepDependencies>
  <properties/>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition" plugin="workflow-cps@3837.v305192405b_c0">
    <scm class="hudson.plugins.git.GitSCM" plugin="git@5.2.1">
      <configVersion>2</configVersion>
      <userRemoteConfigs>
        <hudson.plugins.git.UserRemoteConfig>
          <url>file:///workspace</url>
        </hudson.plugins.git.UserRemoteConfig>
      </userRemoteConfigs>
      <branches>
        <hudson.plugins.git.BranchSpec>
          <name>*/main</name>
        </hudson.plugins.git.BranchSpec>
      </branches>
      <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
      <submoduleCfg class="empty-list"/>
      <extensions/>
    </scm>
    <scriptPath>Jenkinsfile</scriptPath>
    <lightweight>true</lightweight>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
"@

try {
    $createUrl = "$jenkinsUrl/createItem?name=$jobName"
    $response = Invoke-WebRequest -Uri $createUrl -Method POST `
        -ContentType "application/xml" `
        -Body $pipelineConfig `
        -UseBasicParsing
    
    Write-Host "✓ Pipeline créé avec succès!" -ForegroundColor Green
    
    # 3. Vérification
    Write-Host "`n[3/3] Vérification..." -ForegroundColor Yellow
    Start-Sleep -Seconds 2
    $jobUrl = "$jenkinsUrl/job/$jobName"
    $checkResponse = Invoke-WebRequest -Uri "$jobUrl/api/json" -UseBasicParsing
    $jobData = $checkResponse.Content | ConvertFrom-Json
    
    Write-Host "✓ Job accessible!" -ForegroundColor Green
    Write-Host "  Type: $($jobData._class)" -ForegroundColor Gray
    
    Write-Host "`n╔══════════════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║         PIPELINE CRÉÉ AVEC SUCCÈS! 🎉           ║" -ForegroundColor Green
    Write-Host "╚══════════════════════════════════════════════════╝" -ForegroundColor Green
    
    Write-Host "`nURL du Pipeline:" -ForegroundColor Cyan
    Write-Host "  $jobUrl" -ForegroundColor White
    
    Write-Host "`nConfiguration automatique:" -ForegroundColor Cyan
    Write-Host "  ✓ Type: Pipeline (pas Freestyle!)" -ForegroundColor White
    Write-Host "  ✓ SCM: Git" -ForegroundColor White
    Write-Host "  ✓ Repository: file:///workspace" -ForegroundColor White
    Write-Host "  ✓ Branch: */main" -ForegroundColor White
    Write-Host "  ✓ Script: Jenkinsfile" -ForegroundColor White
    
    Write-Host "`n╔══════════════════════════════════════════════════╗" -ForegroundColor Yellow
    Write-Host "║              PROCHAINE ÉTAPE                     ║" -ForegroundColor Yellow
    Write-Host "╚══════════════════════════════════════════════════╝" -ForegroundColor Yellow
    Write-Host "  1. La page Jenkins va s'ouvrir automatiquement" -ForegroundColor White
    Write-Host "  2. Clique sur 'Build Now' (bouton bleu à gauche)" -ForegroundColor White
    Write-Host "  3. Regarde l'exécution en temps réel dans Console Output" -ForegroundColor White
    Write-Host "  4. Attends les 9 stages (≈10-15 minutes)" -ForegroundColor White
    Write-Host "  5. Vérifie les résultats SonarQube sur http://localhost:9000" -ForegroundColor White
    
    # Ouvrir Jenkins dans le navigateur
    Write-Host "`n⏳ Ouverture de Jenkins dans 3 secondes..." -ForegroundColor Gray
    Start-Sleep -Seconds 3
    Start-Process $jobUrl
    
} catch {
    Write-Host "`n✗ Erreur lors de la création:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "  Code HTTP: $statusCode" -ForegroundColor Red
        
        if ($statusCode -eq 400) {
            Write-Host "`n💡 Solution: Le job existe encore. Supprime-le manuellement:" -ForegroundColor Yellow
            Write-Host "  1. Va sur http://localhost:9090" -ForegroundColor White
            Write-Host "  2. Survole 'buy-02-pipeline'" -ForegroundColor White
            Write-Host "  3. Clique sur ▼ puis 'Delete Pipeline'" -ForegroundColor White
            Write-Host "  4. Relance ce script" -ForegroundColor White
        }
    }
}
