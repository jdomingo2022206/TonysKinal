/*
Programador : Jonathan Domingo
Codigo Academico: IN5BV
Carnet: 2022-206
Fecha de Creación: 30/03/2023
Fecha de Modificación: 
Fecha de Finalización: 
*/

Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;
Use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(10) not null,
    primary key PK_codigoEmpresa(codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int auto_increment not null,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado(codigoTipoEmpleado)
);

Create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(10) not null,
    gradoCocinero varchar(50) not null,
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key (codigoTipoEmpleado) 
		references TipoEmpleado(codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int auto_increment not null,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int not null,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)
);

Create table Servicios(
	codigoServicio int auto_increment not null,
    fechaDeServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(10) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Presupuestos(
	codigoPresupuesto int auto_increment not null,
    fechaDeSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuestos_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa) on delete cascade
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150)not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    -- tipoPlato_codigoTipoPlato in not null
    primary key PK_codigoPlato(codigoPlato),
    constraint FK_Platos_TipoPlato1 foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato) on delete cascade
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto(Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos1 foreign key(codigoProducto)
		references Productos(codigoProducto),
	constraint FK_Productos_has_Platos_Platos1 foreign key(codigoPlato)
		references Platos(codigoPlato) on delete cascade
);


Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio(Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Platos_Servicios1 foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos1 foreign key(codigoPlato)
		references Platos(codigoPlato) on delete cascade
		
);

Create table Servicios_has_Empleados(
	Servicio_codigoServicio int not null,
	codigoServicio int not null,
	codigoEmpleado int not null,
    fechaDeEvento date not null,
    horaDeEvento time not null,
    lugarDeEvento varchar(150) not null,
    primary key PK_Servicio_codigoServicio(Servicio_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios1 foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Empleados_Empleados1 foreign key(codigoEmpleado)
		references Empleados(codigoEmpleado) on delete cascade
);

create table Usuario(
	codigoUsuario int not null auto_increment,
	nombreUsuario varchar (100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);

delimiter $$
	create procedure sp_AgregarUsuario (in nombreUsuario varchar(100), in apellidoUsuario varchar(100), 
										in usuarioLogin varchar(50),in contrasena varchar(50))
		Begin
			insert into Usuario (codigoUsuario, nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values (codigoUsuario, nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
		end$$
delimiter ;

delimiter $$
	Create procedure sp_ListarUsuario()
		begin
			select U.codigoUsuario, 
            U.nombreUsuario, 
            U.apellidoUsuario, 
            U.usuarioLogin, 
            U.contrasena
			from Usuario U;
        end$$
delimiter ;

call sp_AgregarUsuario('Jonathan','Domingo','jdomingo','2022206');
call sp_ListarUsuario();

create table Login(
	usuarioMaster varchar(50) not null, 
	usuarioLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);
-- Use DBRecuperacion;

-- ------------------ Procedimientos almacenados Entidad Empresas ----

-- Agregar Empresas

Delimiter $$
	create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(10))
    Begin
		insert into Empresas (nombreEmpresa, direccion, telefono)
			values (nombreEmpresa, direccion, telefono);
    end$$
Delimiter ;

call sp_AgregarEmpresa ('Campero', 'Guatemala Ciudad', '12345678');
call sp_AgregarEmpresa ('Farmacia Galeno', 'Mixco', '13254658');
call sp_AgregarEmpresa ('Maxi', 'Antigua', '25243665');

-- Listar Empresas

Delimiter $$
	create procedure sp_ListarEmpresa()
    begin
		Select E.codigoEmpresa, E.nombreEmpresa, E.direccion, E.telefono from Empresas E;
    end$$
Delimiter ;

call sp_ListarEmpresa();

-- Actualizar Empresa
Describe Empresas;

Delimiter $$
	create procedure sp_ActualizarEmpresa(in codigoEmpresa int, in nombreEmpresa varchar(150), 
										  in direccion varchar(150), in telefono varchar(10))
    begin
		update Empresas E
			set E.nombreEmpresa= nombreEmpresa, 
            E.direccion=direccion, 
            E.telefono= telefono
            where E.codigoEmpresa= codigoEmpresa;
    end$$
Delimiter ;

call sp_ActualizarEmpresa (3,"Walmart","Salama","55585957");

-- Eliminar Empresa
Describe Empresas;

Delimiter $$
	create procedure sp_EliminarEmpresa(in _codigoEmpresa int)
    Begin
		delete from Empresas 
        where codigoEmpresa = _codigoEmpresa;
    end $$
Delimiter ;

call sp_EliminarEmpresa(3);

-- Buscar Empresa

Delimiter $$
	create procedure sp_BuscarEmpresa(in _codigoEmpresa int)
    Begin
		Select E.codigoEmpresa, E.nombreEmpresa, E.direccion, E.telefono from Empresas E
        where E.codigoEmpresa = _codigoEmpresa;
    end $$
Delimiter ;

call sp_BuscarEmpresa(2);


-- ------------------ Procedimientos almacenados Entidad Tipo Empleado ----

-- Agregar TipoEmpleado

Delimiter $$
	create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
	Begin
		insert into TipoEmpleado (descripcion)
			values (descripcion);
	end$$
Delimiter ;

call sp_AgregarTipoEmpleado ('Jefe de Cocina');
call sp_AgregarTipoEmpleado ('Chef de Cocina Fria');
call sp_AgregarTipoEmpleado ('Chef de Cocina Caliente');

-- Listar TipoEmpleado

Delimiter $$
	create procedure sp_ListarTipoEmpleado()
	begin
		Select codigoTipoEmpleado, descripcion from TipoEmpleado;
	end$$
Delimiter ;

call sp_ListarTipoEmpleado();

-- Actualizar TipoEmpleado
Describe TipoEmpleado;

Delimiter $$
	create procedure sp_ActualizarTipoEmpleado(in codigoTipoEmpleado int, in descripcion varchar(50))
	begin
		update TipoEmpleado TE
			set TE.descripcion= descripcion  
			where TE.codigoTipoEmpleado= codigoTipoEmpleado;
	end$$
Delimiter ;

call sp_ActualizarTipoEmpleado (3,"Chef de Postres");

-- Eliminar TipoEmpleado
Describe TipoEmpleado;

Delimiter $$
	create procedure sp_EliminarTipoEmpleado(in _codigoTipoEmpleado int)
	Begin
		delete from TipoEmpleado 
		where codigoTipoEmpleado = _codigoTipoEmpleado;
	end $$
Delimiter ;

call sp_EliminarTipoEmpleado(3);

Delimiter $$
	create procedure sp_BuscarTipoEmpleado(in _codigoTipoEmpleado int)
	Begin
		Select * from TipoEmpleado 
		where codigoTipoEmpleado = _codigoTipoEmpleado;
	end $$
Delimiter ;

call sp_BuscarTipoEmpleado(1);

-- ------------------ Procedimientos almacenados Entidad Empleados ----

-- Agregar Empleado

Delimiter $$
	create procedure sp_AgregarEmpleado( in numeroEmpleado int, in apellidosEmpleado varchar(150), 
									in nombresEmpleado varchar(150), in direccionEmpleado varchar(150), 
									in telefonoContacto varchar(10), in gradoCocinero varchar(50), 
									in codigoTipoEmpleado int)
	Begin
		insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
								telefonoContacto, gradoCocinero, codigoTipoEmpleado)
			values (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
					telefonoContacto, gradoCocinero, codigoTipoEmpleado);
	end$$
Delimiter ;

call sp_AgregarEmpleado (101, "Echeverria Gonzalo", "Marco Papa", "Villa Hermosa", "24895632","Diamante III", 1);
call sp_AgregarEmpleado (102, "Ramirez Coc", "Rafael Santiago", "Carcha", "24891654","Plata II", 2);
call sp_AgregarEmpleado (103, "Rosales Ix", "Carlos Kane", "Landivar", "24897598","Oro I", 2);


-- Listar Empleado

Delimiter $$
	create procedure sp_ListarEmpleado()
	begin
		Select codigoEmpleado,numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, 
				gradoCocinero, codigoTipoEmpleado from Empleados;
	end$$
Delimiter ;

call sp_ListarEmpleado();

-- Actualizar Empleado
Describe Empleados;

Delimiter $$
	create procedure sp_ActualizarEmpleado(in codigoEmpleado int, in numeroEmpleado int, 
											in apellidosEmpleado varchar(150), in nombresEmpleado varchar(150), 
											in direccionEmpleado varchar(150), in telefonoContacto varchar(10), 
											in gradoCocinero varchar(50), in codigoTipoEmpleado int)
	begin
		update Empleados E
			set E.numeroEmpleado= numeroEmpleado,
			E.apellidosEmpleado= apellidosEmpleado, 
			E.nombresEmpleado= nombresEmpleado, 
			E.direccionEmpleado= direccionEmpleado, 
			E.telefonoContacto= telefonoContacto,
			E.gradoCocinero= gradoCocinero, 
			E.codigoTipoEmpleado= codigoTipoEmpleado  
			where E.codigoEmpleado= codigoEmpleado;
	end$$
Delimiter ;

call sp_ActualizarEmpleado (3,103,"Smith McKenzie", "Neymar Gundogan", "Nimayork", "24894699","Oro I", 2);

-- Eliminar Empleado
Describe Empleados;

Delimiter $$
create procedure sp_EliminarEmpleado(in _codigoEmpleado int)
Begin
	delete from Empleados 
	where codigoEmpleado = _codigoEmpleado;
end $$
Delimiter ;

call sp_EliminarEmpleado(3);

Delimiter $$
	create procedure sp_BuscarEmpleado(in codigoEmpleado int)
	begin
		Select codigoEmpleado,numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, 
				gradoCocinero, codigoTipoEmpleado from Empleados where codigoEmpleado=codigoEmpleado; 
	end$$
Delimiter ;

call sp_BuscarEmpleado(1);


-- ------------------ Procedimientos almacenados Entidad TipoPlato ----

-- Agregar TipoPlato

Delimiter $$
	create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
	Begin
		insert into TipoPlato ( descripcionTipo)
			values (descripcionTipo);
	end$$
Delimiter ;

call sp_AgregarTipoPlato ("Ensalada");
call sp_AgregarTipoPlato ("Pollo Asado");
call sp_AgregarTipoPlato ("Rabioles");

-- Listar TipoPlato

Delimiter $$
	create procedure sp_ListarTipoPlato()
	begin
		Select codigoTipoPlato, descripcionTipo from TipoPlato;
	end$$
Delimiter ;

call sp_ListarTipoPlato();

-- Actualizar TipoPlato
Describe TipoPlato;

Delimiter $$
	create procedure sp_ActualizarTipoPlato(in codigoTipoPlato int, 
											 descripcionTipo varchar(100))
	begin
		update TipoPlato TP
			set TP.descripcionTipo = descripcionTipo
			where TP.codigoTipoPlato= codigoTipoPlato;
	end$$
Delimiter ;

call sp_ActualizarTipoPlato (3,"pizza");

-- Busacr TipoPlato

Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codTipoPlato int)
		Begin
			Select 
				T.codigoTipoPlato,
                T.descripcionTipo
                From TipoPlato T where codigoTipoPlato = codTipoPlato;
		End$$
Delimiter ;

call sp_BuscarTipoPlato(1);

-- Eliminar TipoPlato
Describe TipoPlato;

Delimiter $$
create procedure sp_EliminarTipoPlato(in _codigoTipoPlato int)
Begin
	delete from TipoPlato 
	where codigoTipoPlato = _codigoTipoPlato;
end $$
Delimiter ;

call sp_EliminarTipoPlato(3);

-- ------------------ Procedimientos almacenados Entidad Productos ----

-- Agregar Productos

Delimiter $$
	create procedure sp_AgregarProducto(in codigoProducto int, in nombreProducto varchar(150), in cantidad int)
	Begin
		insert into Productos (codigoProducto, nombreProducto, cantidad)
			values (codigoProducto, nombreProducto, cantidad);
	end$$
Delimiter ;

call sp_AgregarProducto (1,"Coca-cola", 10);
call sp_AgregarProducto (2,"Snack", 8);
call sp_AgregarProducto (3,"Frutas", 20);

-- Listar Producto

Delimiter $$
	create procedure sp_ListarProducto()
	begin
		Select codigoProducto, nombreProducto, cantidad from Productos;
	end$$
Delimiter ;

call sp_ListarProducto();
call sp_ListarProducto();

-- Actualizar Producto
Describe Productos;

Delimiter $$
	create procedure sp_ActualizarProducto(in codigoProducto int, 
											 nombreProducto varchar(150), in cantidad int)
	begin
		update Productos P
			set P.nombreProducto = nombreProducto,
			P.cantidad = cantidad
			where P.codigoProducto= codigoProducto;
	end$$
Delimiter ;

call sp_ActualizarProducto(3,"Frutas Tropicales", 27);

-- Eliminar Producto
Describe Productos;

Delimiter $$
create procedure sp_EliminarProducto(in _codigoProducto int)
Begin
	delete from Productos 
	where codigoProducto = _codigoProducto;
end $$
Delimiter ;

call sp_EliminarProducto(3);


Delimiter $$
	Create procedure sp_BuscarProducto(in codProducto int)
		Begin
			Select 
				P.codigoProducto,
                P.nombreProducto,
                P.cantidad
                From Productos P where codigoProducto = codProducto;
		End$$
Delimiter ;

call sp_BuscarProducto(1);



-- ------------------ Procedimientos almacenados Entidad Presupuesto ----
/*
-- Agregar Presupuesto

Delimiter $$
	create procedure sp_AgregarPresupuesto(in codigoProducto int, in nombreProducto varchar(150), in cantidad int)
	Begin
		insert into Productos (codigoProducto, nombreProducto, cantidad)
			values (codigoProducto, nombreProducto, cantidad);
	end$$
Delimiter ;

call sp_AgregarProducto (1,"Coca-cola", 10);
call sp_AgregarProducto (2,"Snack", 8);
call sp_AgregarProducto (3,"Frutas", 20);

-- Listar Producto

Delimiter $$
	create procedure sp_ListarProducto()
	begin
		Select codigoProducto, nombreProducto, cantidad from Productos;
	end$$
Delimiter ;

call sp_ListarProducto();

-- Actualizar Producto
Describe Productos;

Delimiter $$
	create procedure sp_ActualizarProducto(in codigoProducto int, 
											 nombreProducto varchar(150), in cantidad int)
	begin
		update Productos P
			set P.nombreProducto = nombreProducto,
			P.cantidad = cantidad
			where P.codigoProducto= codigoProducto;
	end$$
Delimiter ;

call sp_ActualizarProducto(3,"Frutas Tropicales", 27);

-- Eliminar Producto
Describe Productos;

Delimiter $$
create procedure sp_EliminarProducto(in _codigoProducto int)
Begin
	delete from Productos 
	where codigoProducto = _codigoProducto;
end $$
Delimiter ;

call sp_EliminarProducto(3);
*/



Delimiter $$
	create procedure sp_ListarPresupuesto()
	begin
		Select codigoPresupuesto, fechaDeSolicitud, cantidadPresupuesto, codigoEmpresa from Presupuestos;
	end$$
Delimiter ;

call sp_ListarPresupuesto();


Delimiter $$
	Create procedure sp_AgregarPresupuesto(in fechaDeSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
	Begin
		Insert into Presupuestos(fechaDeSolicitud, cantidadPresupuesto, codigoEmpresa)
        values (fechaDeSolicitud, cantidadPresupuesto, codigoEmpresa);
        End$$
Delimiter ;

Call sp_AgregarPresupuesto(now(), 125.14, 1);
Call sp_AgregarPresupuesto(now(), 150.75, 2);
Call sp_AgregarPresupuesto(now(), 45125.20, 1);
Call sp_AgregarPresupuesto(now(), 1548.50, 2);

Delimiter $$
	Create procedure sp_EliminarPresupuesto(in _codigoPresupuesto int)
	Begin
		delete from Presupuestos 
        where codigoPresupuesto = _codigoPresupuesto;
        End$$
Delimiter ;

call sp_EliminarPresupuesto(4);

Delimiter $$
	Create procedure sp_ActualizarPresupuesto(in _codigoPresupuesto int, in fechaDeSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
	Begin
		update Presupuestos
		set fechaDeSolicitud=fechaDeSolicitud, 
        cantidadPresupuesto=cantidadPresupuesto,
        codigoEmpresa =codigoEmpresa
        where codigoPresupuesto = _codigoPresupuesto;
        End$$
Delimiter ;

 call sp_ActualizarPresupuesto(3,now(),121.00, 1);

/*
Delimiter $$
	Create procedure sp_ActualizarPresupuestos(in _codigoPresupuesto int, in fechaDeSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
	Begin
		update Presupuestos
		set fechaDeSolicitud=fechaDeSolicitud, 
        cantidadPresupuesto=cantidadPresupuesto,
        codigoEmpresa = codigoEmpresa
        where codigoPresupuesto = _codigoPresupuesto;
        End$$
Delimiter ;

call sp_ActualizarPresupuestos(3,now(),121.00,2);*/

/*
Create table (
	codigo int not null,
    primary key PK_codigo(codigo)
);

constraint foreign key()
		references()
*/

-- --------------- Procedimientos almacenados Entidad Platos ----------------------

-- Agregar Platos---
Delimiter $$
	Create procedure sp_AgregarPlatos ( in cantidad int, in nombrePlato varchar(50), in descripcionPlato varchar(150),
		in precioPlato decimal(10,2), in codigoTipoPlato int)
		Begin 
			Insert into Platos (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values (cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End$$
Delimiter ;

Call sp_AgregarPlatos (4, "Ensalada César", "Hecha con lechuga romana fresca, crutones, queso parmesano y aderezado con una sabrosa salsa César", 15.50, 1 );
Call sp_AgregarPlatos (6, "Paella", "Hecho con arroz, mariscos, salchichas y una variedad de verduras, todo cocido en un caldo de mariscos", 424.25, 2);
Call sp_AgregarPlatos (9, "Flan", "hecho con leche, huevos, azúcar y vainilla, horneado a fuego lento y cubierto con un caramelo pegajoso", 10.00, 2);	


-- Listar Platos---
Delimiter $$
	Create procedure sp_ListarPlatos()
		Begin 
			Select 
				P.codigoPlato,
                P.nombrePlato,
                P.descripcionPlato,
                P.cantidad,
                P.precioPlato,
                P.codigoTipoPlato
                From Platos P;
        End$$
Delimiter ;

Call sp_ListarPlatos();
-- Call sp_ListarServicios();

-- Buscar Platos---
Delimiter $$
	Create procedure sp_BuscarPlatos (in codPlato int)
		Begin
			Select 
				P.codigoPlato,
                P.cantidad,
                P.nombrePlato,
                P.descripcionPlato,
                P.precioPlato,
                P.codigoTipoPlato
                From Platos P where codigoPlato = codPlato;
		End$$
Delimiter ;

 call sp_BuscarPlatos(2);

-- Eliminar Platos---
Delimiter $$
	Create procedure sp_EliminarPlatos (in codPlato int)
		Begin 
			Delete from Platos
				where codigoPlato = codPlato;
        End$$
Delimiter ;

-- Call sp_EliminarPlatos(1);

-- Editar Platos---
Delimiter $$
	Create procedure sp_EditarPlatos(in codPlato int , in cant int, in nomPlato varchar(50), in descripPlato varchar(150),
		in preciPlato decimal(10,2), in codTipoPlato int)
		Begin 
			update Platos P
				set P.codigoPlato = codPlato,
					P.cantidad = cant,
					P.nombrePlato = nomPlato,
					P.descripcionPlato = descripPlato,
                    P.precioPlato = preciPlato,
                    P.codigoTipoPlato = codTipoPlato
				where codigoPlato = codPlato;
        End$$
Delimiter ;

-- Call sp_EditarPlatos(2, "");


-- --------------- Procedimientos almacenados Entidad Servicios ----------------------

-- Agregar Servicios---
Delimiter $$
	Create procedure sp_AgregarServicios (in fechaServicio date, in tipoServicio varchar(150), in horaServicio time,
		in lugarServicio varchar(150), in telefonoContacto varchar(10), in codigoEmpresa int)
		Begin 
			Insert into Servicios (fechaDeServicio, tipoServicio, horaServicio, lugarServicio, 
				telefonoContacto, codigoEmpresa)
				values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End$$
Delimiter ;

Call sp_AgregarServicios ("2023-05-21", "Buffet", "11:30:00", "4a. Avenida y 2a. Calle 1-71 Zona 2", "36472835", 1);
Call sp_AgregarServicios ("2023-09-14", "Banquetes", "14:00:00", "6a. Avenida 4-13 Zona 1", "77481932", 2);
Call sp_AgregarServicios ("2023-07-05", "Degustación", "20:10:00", "Calzada Roosevelt 13-46 zona 7", "67291054", 2);

-- Listar Servicios---
Delimiter $$
	Create procedure sp_ListarServicios()
		Begin 
			Select 
				S.codigoServicio, 
                S.fechaDeServicio, 
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
                From Servicios S;
        End$$
Delimiter ;

Call sp_ListarServicios();

-- Buscar Servicios---
Delimiter $$
	Create procedure sp_BuscarServicios(in codServicio int)
		Begin
			Select 
				S.codigoServicio, 
                S.fechaDeServicio, 
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa
                From Servicios S where codigoServicio = codServicio;
		End$$
Delimiter ;

-- Call sp_BuscarServicios(2);

-- Eliminar Servicios---
Delimiter $$
	Create procedure sp_EliminarServicios(in codServicio int)
		Begin 
			Delete from Servicios
				where codigoServicio = codServicio;
        End$$
Delimiter ;

-- Call sp_EliminarServicios(1);

-- Editar Servicios---
Delimiter $$
	Create procedure sp_EditarServicios(in codServicio int , in fechaServi date, in tipServicio varchar(150), in hrServicio time,
		in lugarServi varchar(150), in telContacto varchar(10), in codEmpresa int)
		Begin 
			update Servicios S
				set S.codigoServicio = codServicio, 
					S.fechaDeServicio = fechaServi, 
					S.tipoServicio = tipServicio,
					S.horaServicio = hrServicio,
					S.lugarServicio = lugarServi,
					S.telefonoContacto = telContacto,
					S.codigoEmpresa = codEmpresa
				where codigoServicio = codServicio;
        End$$
Delimiter ;



-- --------------- Procedimientos almacenados Entidad Productos_has_Platos ----------------------

-- Agregar Productos_has_Platos---
Delimiter $$
	Create procedure sp_AgregarProductos_has_Platos (in Productos_codigoProducto int, in codigoPlato int, in codigoProducto int)
		Begin 
			Insert into Productos_has_Platos (Productos_codigoProducto, codigoPlato, codigoProducto)
				values (Productos_codigoProducto, codigoPlato, codigoProducto);
        End$$
Delimiter ;

Call sp_AgregarProductos_has_Platos (50, 1, 1);
Call sp_AgregarProductos_has_Platos (51, 2, 2);
Call sp_AgregarProductos_has_Platos (52, 3, 2);

Call sp_ListarProducto();
Call sp_ListarPlatos();

-- Listar Productos_has_Platos---
Delimiter $$
	Create procedure sp_ListarProductos_has_Platos()
		Begin 
			Select 
				P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                From Productos_has_Platos P;
        End$$
Delimiter ;

Call sp_ListarProductos_has_Platos();

-- Buscar Productos_has_Platos---
Delimiter $$
	Create procedure sp_BuscarProductos_has_Platos (in Productos_codProducto int)
		Begin
			Select 
				P.Productos_codigoProducto,
                P.codigoPlato,
                P.codigoProducto
                From Productos_has_Platos P where Productos_codigoProducto = Productos_codProducto;
		End$$
Delimiter ;

-- Call sp_BuscarProductos_has_Platos(2);

-- Eliminar Productos_has_Platos---
Delimiter $$
	Create procedure sp_EliminarProductos_has_Platos (in Productos_codProducto int)
		Begin 
			Delete from Productos_has_Platos
				where Productos_codigoProducto = Productos_codProducto;
        End$$
Delimiter ;

-- Call sp_EliminarProductos_has_Platos(1);

-- Editar Productos_has_Platos---
Delimiter $$
Create procedure sp_EditarProductos_has_Platos(in Productos_codigoProd int, in codPlato int, in codProducto int)
		Begin 
			update Productos_has_Platos P
				set P.Productos_codigoProducto = Productos_codigoProd,
					P.codigoPlato = codPlato,
					P.codigoProducto = codProducto
				where Productos_codigoProducto = Productos_codigoProd;
        End$$
Delimiter ;

-- Call sp_EditarProductos_has_Platos(2, "");


-- --------------- Procedimientos almacenados Entidad Servicios_has_Platos ----------------------

-- Agregar Servicios_has_Platos---
Delimiter $$
	Create procedure sp_AgregarServicios_has_Platos (in Servicios_codigoServicio int, in codigoPlato int, in codigoServicio int)
		Begin 
			Insert into Servicios_has_Platos (Servicios_codigoServicio, codigoPlato, codigoServicio)
				values (Servicios_codigoServicio, codigoPlato, codigoServicio);
        End$$
Delimiter ;

describe Servicios_has_Platos;

Call sp_AgregarServicios_has_Platos (60, 1, 3);
Call sp_AgregarServicios_has_Platos (61, 2, 2);
Call sp_AgregarServicios_has_Platos (62, 3, 1);


-- Listar Servicios_has_Platos---
Delimiter $$
	Create procedure sp_ListarServicios_has_Platos()
		Begin 
			Select 
				S.Servicios_codigoServicio,
                S.codigoPlato,
                S.codigoServicio
                From Servicios_has_Platos S;
        End$$
Delimiter ;

Call sp_ListarServicios_has_Platos();

-- Buscar Servicios_has_Platos---
Delimiter $$
	Create procedure sp_BuscarServicioss_has_Platos (in Servicios_codServicio int)
		Begin
			Select 
				S.Servicios_codigoServicio,
                S.codigoPlato,
                S.codigoServicio
                From Servicios_has_Platos S where Servicios_codigoServicio = Servicios_codServicio;
		End$$
Delimiter ;

-- Call sp_BuscarServicioss_has_Platos(2);

-- Eliminar Servicios_has_Platos---
Delimiter $$
	Create procedure sp_EliminarServicios_has_Platos (in Servicios_codServicio int)
		Begin 
			Delete from Servicios_has_Platos
				where Servicios_codigoServicio = Servicios_codServicio;
        End$$
Delimiter ;

-- Call sp_EliminarServicios_has_Platos(1);

-- Editar Servicios_has_Platos---
Delimiter $$
Create procedure sp_EditarServicios_has_Platos(in Servicios_codServicio int, in codPlato int, in codServicio int)
		Begin 
			update Productos_has_Platos P
				set P.Servicios_codigoServicio = Servicios_codServicio,
					P.codigoPlato = codPlato,
					P.codigoServicio = codServicio
				where Servicios_codigoServicio = Servicios_codServicio;
        End$$
Delimiter ;

-- Call sp_EditarServicios_has_Platos(2, "");


-- --------------- Procedimientos almacenados Entidad Servicios_has_Empleados ----------------------

-- Agregar Servicios_has_Empleados---
Delimiter $$
	Create procedure sp_AgregarServicios_has_Empleados (in Servicios_codigoServicio int, in codigoEmpleado int, in codigoServicio int,
		in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
		Begin 
			Insert into Servicios_has_Empleados (Servicio_codigoServicio, codigoEmpleado, codigoServicio, fechaDeEvento, horaDeEvento, lugarDeEvento)
				values (Servicios_codigoServicio, codigoEmpleado, codigoServicio, fechaEvento, horaEvento, lugarEvento);
        End$$
Delimiter ;

describe Servicios_has_Empleados;
Call sp_AgregarServicios_has_Empleados (70, 2, 2, "2023-07-25", "17:20:00", "Ave. Reforma 2-18 zona 9, Edificio Cortijo Reforma");
Call sp_AgregarServicios_has_Empleados (71, 1, 1, "2023-11-14", "10:40:00", "1era. Calle 3-87 zona 5");
Call sp_AgregarServicios_has_Empleados (72, 1, 3, "2023-06-29", "20:10:00", "6a Ave. 10-61 zona 4");

-- Listar Servicios_has_Empleados---
Delimiter $$
	Create procedure sp_ListarServicios_has_Empleados()
		Begin 
			Select 
				SE.Servicio_codigoServicio,
                SE.codigoEmpleado,
                SE.codigoServicio,
                SE.fechaDeEvento,
                SE.horaDeEvento,
                SE.lugarDeEvento
                From Servicios_has_Empleados SE;
        End$$
Delimiter ;
describe Servicios_has_Empleados;
Call sp_ListarServicios_has_Empleados();

-- Buscar Servicios_has_Empleados---
Delimiter $$
	Create procedure sp_BuscarServicioss_has_Empleados (in Servicios_codServicio int)
		Begin
			Select 
				SE.Servicio_codigoServicio,
                SE.codigoEmpleado,
                SE.codigoServicio,
                SE.fechaDeEvento,
                SE.horaDeEvento,
                SE.lugarDeEvento
                From Servicios_has_Empleados SE where Servicios_codigoServicio = Servicios_codServicio;
		End$$
Delimiter ;

-- Call sp_BuscarServicioss_has_Empleados(2);

-- Eliminar Servicios_has_Empleados---
Delimiter $$
	Create procedure sp_EliminarServicios_has_Empleados (in Servicios_codServicio int)
		Begin 
			Delete from Servicios_has_Empleados
				where Servicio_codigoServicio = Servicios_codServicio;
        End$$
Delimiter ;

-- Call sp_EliminarServicios_has_Empleados(1);

-- Editar Servicios_has_Empleados---
Delimiter $$
Create procedure sp_EditarServicios_has_Empleados(in Servicios_codServicio int, in codEmpleado int, in codServicio int,
		in fchEvento date, in horEvento time, in lgrEvento varchar(150))
		Begin 
			update Productos_has_Empleados E
				set E.Servicios_codigoServicio = Servicios_codServicio,
					E.codigoEmpleado = codEmpleado,
					E.codigoServicio = codServicio,
                    E.fechaEvento = fchEvento,
                    E.horaEvento = horEvento,
                    E.lugarEvento = lgrEvento
				where Servicios_codigoServicio = Servicios_codServicio;
        End$$
Delimiter ;

-- Call sp_EditarServicios_has_Empleados(2, "");

-- Procedimiento para el Reporte General
Delimiter $$
    Create procedure sp_ReporteGeneral()
        Begin
            Select * From Empresas E Inner Join Presupuestos P on E.codigoEmpresa = P.codigoEmpresa
                Inner join Servicios S on E.codigoEmpresa = S.codigoEmpresa
				Inner join  Servicios_has_Empleados ShE on S.codigoServicio = ShE.codigoServicio
				Inner join Empleados EM on ShE.codigoEmpleado = EM.codigoEmpleado
				Inner join TipoEmpleado TE on EM.codigoTipoEmpleado = TE.codigoTipoEmpleado
				Inner join Servicios_has_Platos ShP on S.codigoServicio = ShP.codigoServicio
				Inner join Platos PL on ShP.codigoPlato = PL.codigoPlato
				Inner join TipoPlato TP on PL.codigoTipoPlato = TP.codigoTipoPlato
				Inner join Productos_has_Platos PhP on PL.codigoPlato = PhP.codigoPlato
				Inner join Productos Pr on PhP.codigoProducto = Pr.codigoProducto;
        End $$
Delimiter ;

Call sp_ReporteGeneral();
