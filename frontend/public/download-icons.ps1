# Download favicon.ico
Invoke-WebRequest -Uri "https://raw.githubusercontent.com/facebook/create-react-app/main/packages/cra-template/template/public/favicon.ico" -OutFile "favicon.ico"

# Download logo192.png
Invoke-WebRequest -Uri "https://raw.githubusercontent.com/facebook/create-react-app/main/packages/cra-template/template/public/logo192.png" -OutFile "logo192.png"

# Download logo512.png
Invoke-WebRequest -Uri "https://raw.githubusercontent.com/facebook/create-react-app/main/packages/cra-template/template/public/logo512.png" -OutFile "logo512.png"

Write-Host "Icons downloaded successfully!" 