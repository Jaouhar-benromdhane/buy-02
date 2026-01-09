# Script pour créer le job Pipeline Jenkins automatiquement
Write-Host "=== Création du Pipeline Jenkins buy-02-pipeline ===" -ForegroundColor Cyan

$jenkinsUrl = "http://localhost:9090"
$jobName = "buy-02-pipeline"

# Configuration XML du pipeline
$pipelineConfig = @"
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job@1400.v7fd111b_ec82f">
  <description>Pipeline CI/CD pour buy-02 ecommerce</description>
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
    Write-Host "`n[1/2] Création du job Pipeline..." -ForegroundColor Yellow
    
    # Créer le job via l'API Jenkins
    $createUrl = "$jenkinsUrl/createItem?name=$jobName"
    $response = Invoke-WebRequest -Uri $createUrl -Method POST `
        -ContentType "application/xml" `
        -Body $pipelineConfig `
        -UseBasicParsing
    
    Write-Host "✓ Job créé avec succès!" -ForegroundColor Green
    
    Write-Host "`n[2/2] Vérification..." -ForegroundColor Yellow
    $jobUrl = "$jenkinsUrl/job/$jobName"
    Start-Sleep -Seconds 2
    
    $checkResponse = Invoke-WebRequest -Uri "$jobUrl/api/json" -UseBasicParsing
    Write-Host "✓ Job accessible!" -ForegroundColor Green
    
    Write-Host "`n╔══════════════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║         PIPELINE CRÉÉ AVEC SUCCÈS! 🎉           ║" -ForegroundColor Green
    Write-Host "╚══════════════════════════════════════════════════╝" -ForegroundColor Green
    
    Write-Host "`nURL du Pipeline:" -ForegroundColor Cyan
    Write-Host "  $jobUrl" -ForegroundColor White
    
    Write-Host "`nConfiguration:" -ForegroundColor Cyan
    Write-Host "  • Type: Pipeline" -ForegroundColor White
    Write-Host "  • SCM: Git" -ForegroundColor White
    Write-Host "  • Repository: file:///workspace" -ForegroundColor White
    Write-Host "  • Branch: */main" -ForegroundColor White
    Write-Host "  • Script: Jenkinsfile" -ForegroundColor White
    
    Write-Host "`nProchaine étape:" -ForegroundColor Yellow
    Write-Host "  1. Ouvre l'URL ci-dessus" -ForegroundColor White
    Write-Host "  2. Clique sur 'Build Now' pour lancer le pipeline" -ForegroundColor White
    Write-Host "  3. Regarde la console output en temps réel" -ForegroundColor White
    
    # Ouvrir Jenkins dans le navigateur
    Start-Process $jobUrl
    
} catch {
    Write-Host "`n✗ Erreur lors de la création du job:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "`nLe job existe peut-être déjà. Vérifie:" -ForegroundColor Yellow
        Write-Host "  $jobUrl" -ForegroundColor White
    }
}
