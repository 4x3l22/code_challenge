Param(
  # Carpetas de microservicios (ajusta la lista si agregas más)
  [string[]] $ServicesDirs = @("IAM-SERVICE", "LOAN-SERVICE"),
  # Acción: up / down / restart / build
  [ValidateSet("up","down","restart","build")] [string] $Action = "up",
  # Forzar build en "up"
  [switch] $Rebuild,
  # Profile a usar en cada compose (si no usas profiles, déjalo en "all" o quítalo)
  [string] $Profile = "all"
)

$ErrorActionPreference = "Stop"

# Crea la red compartida si no existe
Write-Host ">> Verificando red 'crediya-network'..."
docker network inspect crediya-network *> $null 2>&1
if ($LASTEXITCODE -ne 0) {
  docker network create crediya-network | Out-Null
  Write-Host "   Creada red 'crediya-network'."
} else {
  Write-Host "   OK, red existente."
}

# Posibles nombres de archivo compose por microservicio
$composeCandidates = @(
  "docker-compose.yml","docker-compose.yaml",
  "compose.yml","compose.yaml",
  "compose-iam.yml","compose-loan.yml"
)

foreach ($dir in $ServicesDirs) {
  if (-not (Test-Path $dir)) {
    Write-Warning "⚠️  Carpeta no encontrada: $dir  (se omite)"
    continue
  }

  # Busca el archivo compose dentro de la carpeta
  $composeFile = $null
  foreach ($cand in $composeCandidates) {
    $p = Join-Path $dir $cand
    if (Test-Path $p) { $composeFile = $cand; break }
  }

  if (-not $composeFile) {
    Write-Warning "⚠️  No se encontró archivo compose en $dir (nombres probados: $($composeCandidates -join ', '))"
    continue
  }

  Push-Location $dir
  try {
    # Construye los argumentos según la acción
    switch ($Action) {
      "up" {
        $args = @("-f", $composeFile, "up", "-d")
        if ($Rebuild) { $args += "--build" }
        # Si usas profiles en los microservicios, pásalo; si no, comenta estas 2 líneas:
        $args = @("-f", $composeFile, "--profile", $Profile, "up", "-d") + ($(if ($Rebuild) { "--build" } else { @() }))
      }
      "down"    { $args = @("-f", $composeFile, "down", "-v") }
      "restart" { $args = @("-f", $composeFile, "restart") }
      "build"   { $args = @("-f", $composeFile, "build") }
    }

    Write-Host ">>> $dir : docker compose $($args -join ' ')"
    docker compose @args

    # (Opcional) muestra contenedores levantados de este compose
    docker compose -f $composeFile ps
  }
  finally {
    Pop-Location
  }
}
