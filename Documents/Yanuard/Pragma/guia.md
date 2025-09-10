#levantar el aplicativo 
.\orchestrate.ps1 -Action up -Rebuild -ServicesDirs @("IAM-SERVICE","LOAN-SERVICE") -Profile all
