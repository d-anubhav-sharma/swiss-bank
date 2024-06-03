if "%1"=="" (
    echo "Service name argument required";
    exit 1;
)
docker compose up -d --no-deps --build %1