USE Master
GO

--Si existe la BD la eliminamos.
IF EXISTS( SELECT Name FROM sys.sysdatabases WHERE Name = 'docmanager' )
DROP DATABASE docmanager
GO

--Creando la BD.
CREATE DATABASE docmanager
GO

--Si existe el Inicio de sesión lo eliminamos.
IF EXISTS( SELECT name FROM sys.syslogins WHERE name = 'docmanager' )
DROP LOGIN docmanager
GO

--Creando el inicio de sesión.
CREATE LOGIN docmanager
WITH PASSWORD = 'qwerty',
CHECK_POLICY = OFF,
CHECK_EXPIRATION = OFF
GO

--Usamos la BD que creamos 'docmanager'
USE docmanager
GO

--Creamos el usuario a la BD, el usuario debe de ir asociado a un inicio de sesión.
CREATE USER docmanager
FOR LOGIN docmanager
GO

--Le agregamos un Rol al Usuario.
sp_addrolemember 'db_owner', 'docmanager'
GO

