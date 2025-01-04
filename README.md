Instalar Visual Studio Code (Para la web)

Instalar Android Studio (Para la app móvil)

Instalar Visual Studio (Para la api)




===============INICIO CONFIGURACIÓN DEL ENTORNO PARA EJECUTAR LA APP WEB===============


Instalar Node.js (https://nodejs.org/es)

Comprobamos la instalación con los siguientes comandos:

node -v

npm -v

Instalar Angular CLI
npm install -g @angular/cli

**
Si nos sale un error similar a este:

npm : No se puede cargar el archivo C:\Program Files\nodejs\npm.ps1 porque la ejecución de scripts está deshabilitada en este sistema. Para obtener más información, consulta el tema about_Execution_Policies en 
https:/go.microsoft.com/fwlink/?LinkID=135170.

Ejecutamos -> Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
Para cambiar temporalmente la política de ejecución para permitir la ejecución de scripts

Una vez hemos cambiado la política de ejecución de scripts, instalamos Angular CLI
**

**
Si al ejecutar -> npm install -g @angular/cli
Nos sale algún error por alguna dependencia, podemos forzar la instalación con "npm i --force", así se instalarán todas las dependencias definidas en el angular.json.
**

Iniciamos la aplicación
ng serve

===============INICIO CONFIGURACIÓN DEL ENTORNO PARA EJECUTAR LA APP MÓVIL===============

En la app móvil hay que modificar los siguientes archivos para poder probar el proyecto en local:

RotrofitObject (es.jac.roncafit.managers)
network_security_config.xml (res/xml)

Aquí debemos cambiar la IP por la de nuestro equipo
