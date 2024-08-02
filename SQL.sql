﻿CREATE DATABASE QuanLyQuanBiDaFinal1
GO

USE QuanLyQuanBidaFinal1
GO

CREATE TABLE TableBida
(
	id INT IDENTITY PRIMARY KEY,
	name NVARCHAR(100) NOT NULL DEFAULT N'Bàn chưa có tên',
	status NVARCHAR(100) NOT NULL DEFAULT N'Trống'	,
)
GO
CREATE TABLE Account
(
	UserName NVARCHAR(100) PRIMARY KEY,	
	DisplayName NVARCHAR(100) NOT NULL DEFAULT N'Staff',
	PassWord NVARCHAR(1000) NOT NULL DEFAULT 0,
	Type INT NOT NULL  DEFAULT 0 -- 1: admin && 0: staff
)
Go

CREATE TABLE FoodCategory
(
	id INT IDENTITY PRIMARY KEY,
	name NVARCHAR(100) NOT NULL DEFAULT N'Chưa đặt tên'
)
GO

CREATE TABLE Food
(
	id INT IDENTITY PRIMARY KEY,
	name NVARCHAR(100) NOT NULL DEFAULT N'Chưa đặt tên',
	idCategory INT NOT NULL,
	price FLOAT NOT NULL DEFAULT 0
	
	FOREIGN KEY (idCategory) REFERENCES dbo.FoodCategory(id)
)
GO

CREATE TABLE Bill
(
	id INT IDENTITY PRIMARY KEY,
	DateCheckIn DATETIME NOT NULL DEFAULT GETDATE(),
	DateCheckOut DATETIME,
	idTable INT NOT NULL,
	status INT NOT NULL DEFAULT 0 -- 1: đã thanh toán && 0: chưa thanh toán
	
	FOREIGN KEY (idTable) REFERENCES dbo.TableBida(id)
)
GO


CREATE TABLE BillInfo
(
	id INT IDENTITY PRIMARY KEY,
	idBill INT NOT NULL,
	idFood INT NOT NULL,
	count INT NOT NULL DEFAULT 0
	
	FOREIGN KEY (idBill) REFERENCES dbo.Bill(id),
	FOREIGN KEY (idFood) REFERENCES dbo.Food(id)
)
GO

CREATE TABLE Customer (
	idcustomer varchar(100) PRIMARY KEY,
	name nvarchar(100),
	gender nvarchar(100),
	phonenumber nvarchar(100),
	daycheckin varchar(100),
	idcategorycustomer nvarchar(100),

);

INSERT INTO dbo.Account
        ( UserName ,
          DisplayName ,
          PassWord ,
          Type
        )
VALUES  ( N'Admin' , -- UserName - nvarchar(100)
          N'Admin' , -- DisplayName - nvarchar(100)
          N'1' , -- PassWord - nvarchar(1000)
          1  -- Type - int
        )
INSERT INTO dbo.Account
        ( UserName ,
          DisplayName ,
          PassWord ,
          Type
        )
VALUES  ( N'Staff' , -- UserName - nvarchar(100)
          N'Staff' , -- DisplayName - nvarchar(100)
          N'1' , -- PassWord - nvarchar(1000)
          0  -- Type - int
        )
GO

CREATE PROC USP_GetAccountByUserName
@userName nvarchar(100)
AS 
BEGIN
	SELECT * FROM dbo.Account WHERE UserName = @userName
END
GO

EXEC dbo.USP_GetAccountByUserName @userName = N'Admin' -- nvarchar(100)

GO

CREATE PROC USP_Login
@userName nvarchar(100), @passWord nvarchar(100)
AS
BEGIN
	SELECT * FROM dbo.Account WHERE UserName = @userName AND PassWord = @passWord
END
GO

-- thêm bàn
DECLARE @i INT = 1

WHILE @i <= 20
BEGIN
	INSERT dbo.TableBida( name)VALUES  ( N'Bàn ' + CAST(@i AS nvarchar(100)))
	SET @i = @i + 1
END
GO

CREATE PROC USP_GetTableList
AS SELECT * FROM dbo.TableBida
GO

UPDATE dbo.TableFood SET STATUS = N'Có người' WHERE id = 9

EXEC dbo.USP_GetTableList
GO

-- thêm category
INSERT dbo.FoodCategory
        ( name )
VALUES  ( N'Nước Ngọt'  -- name - nvarchar(100)
          )
INSERT dbo.FoodCategory
        ( name )
VALUES  ( N'Trái cây' )
INSERT dbo.FoodCategory
        ( name )
VALUES  ( N'Bia' )

-- thêm món ăn
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Sting', -- name - nvarchar(100)
          1, -- idCategory - int
          150000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Pessi', 1, 15000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Coca', 1, 15000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'7Up', 1, 15000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Bò cụng', 1, 999999)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Mận', 2, 25000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Ổi', 2, 20000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Nho', 2, 30000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Thơm Thái', 2, 20000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Dưa Hấu', 2, 18000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Sài Gòn', 3, 30000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Tiger', 3, 40000)
INSERT dbo.Food
        ( name, idCategory, price )
VALUES  ( N'Heniken', 2, 50000)

-- thêm bill

declare @i INT =1
While @i <=19
Begin
	Insert dbo.BILL(DateChecKIn,DateCheckOut,idTable,status) values (GETDATE(),null,@i,0)
set @i = @i+1
End

-- thêm bill info
insert BillInfo(idBill,idFood,count) values(20,1,3)
go
insert BillInfo(idBill,idFood,count) values(21,4,2)
go
insert BillInfo(idBill,idFood,count) values(22,3,3)
go
insert BillInfo(idBill,idFood,count) values(4,2,1)
go
insert BillInfo(idBill,idFood,count) values(5,5,4)
go
insert BillInfo(idBill,idFood,count) values(6,2,3)
go
insert BillInfo(idBill,idFood,count) values(7,1,2)
go
insert BillInfo(idBill,idFood,count) values(8,2,4)
go
insert BillInfo(idBill,idFood,count) values(9,6,6)
go
insert BillInfo(idBill,idFood,count) values(10,11,8)
go
insert BillInfo(idBill,idFood,count) values(11,1,3)
go
insert BillInfo(idBill,idFood,count) values(12,1,2)
go
insert BillInfo(idBill,idFood,count) values(13,2,3)
go
insert BillInfo(idBill,idFood,count) values(14,6,3)
go
insert BillInfo(idBill,idFood,count) values(15,2,2)
go
insert BillInfo(idBill,idFood,count) values(16,3,5)
go
insert BillInfo(idBill,idFood,count) values(17,2,2)
go
insert BillInfo(idBill,idFood,count) values(18,6,4)
go
insert BillInfo(idBill,idFood,count) values(19,3,6)
go

ALTER TABLE dbo.Bill
ADD discount INT

UPDATE dbo.Bill SET discount = 0
GO

CREATE PROC USP_InsertBill
@idTable INT
AS
BEGIN
	INSERT dbo.Bill 
	        ( DateCheckIn ,
	          DateCheckOut ,
	          idTable ,
	          status,
	          discount
	        )
	VALUES  ( GETDATE() , -- DateCheckIn - date
	          NULL , -- DateCheckOut - date
	          @idTable , -- idTable - int
	          0,  -- status - int
	          0
	        )
END
GO

CREATE PROC USP_InsertBillInfo
@idBill INT, @idFood INT, @count INT
AS
BEGIN
DECLARE @isExitsBillInfo INT
	DECLARE @foodCount INT = 1
	
	SELECT @isExitsBillInfo = id, @foodCount = b.count 
	FROM dbo.BillInfo AS b 
	WHERE idBill = @idBill AND idFood = @idFood

	IF (@isExitsBillInfo > 0)
	BEGIN
		DECLARE @newCount INT = @foodCount + @count
		IF (@newCount > 0)
			UPDATE dbo.BillInfo	SET count = @foodCount + @count WHERE idFood = @idFood
		ELSE
			DELETE dbo.BillInfo WHERE idBill = @idBill AND idFood = @idFood
	END
	ELSE
	BEGIN
		INSERT	dbo.BillInfo
        ( idBill, idFood, count )
		VALUES  ( @idBill, -- idBill - int
          @idFood, -- idFood - int
          @count  -- count - int
          )
	END
END
GO

DELETE dbo.BillInfo

DELETE dbo.Bill

CREATE TRIGGER UTG_UpdateBillInfo
ON dbo.BillInfo FOR INSERT, UPDATE
AS
BEGIN
	DECLARE @idBill INT
	
	SELECT @idBill = idBill FROM Inserted
	
	DECLARE @idTable INT
	
	SELECT @idTable = idTable FROM dbo.Bill WHERE id = @idBill AND status = 0	
	
	DECLARE @count INT
	SELECT @count = COUNT(*) FROM dbo.BillInfo WHERE idBill = @idBill
	
	IF (@count > 0)
	BEGIN
	
		PRINT @idTable
		PRINT @idBill
		PRINT @count
		
		UPDATE dbo.TableBida SET status = N'Có người' WHERE id = @idTable		
		
	END		
	ELSE
	BEGIN
	PRINT @idTable
		PRINT @idBill
		PRINT @count
	UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable	
	end
	
END
GO


CREATE TRIGGER UTG_UpdateBill
ON dbo.Bill FOR UPDATE
AS
BEGIN
	DECLARE @idBill INT
	
	SELECT @idBill = id FROM Inserted
	
	DECLARE @idTable INT
	
	SELECT @idTable = idTable FROM dbo.Bill WHERE id = @idBill
	
	DECLARE @count int = 0
	
	SELECT @count = COUNT(*) FROM dbo.Bill WHERE idTable = @idTable AND status = 0
	
	IF (@count = 0)
		UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable
END
GO



--PROC Chuyển bàn2
exec USP_SwitchTabel 7,4

CREATE PROC USP_SwitchTabel
@idTable1 INT, @idTable2 int
AS 
BEGIN

	DECLARE @idFirstBill INT
	DECLARE @idSecondBill INT
	
	DECLARE @isFirstTablEmty INT = 1
	DECLARE @isSecondTablEmty INT = 1
	
	
	SELECT @idSecondBill = id FROM dbo.Bill WHERE idTable = @idTable2 AND status = 0
	SELECT @idFirstBill = id FROM dbo.Bill WHERE idTable = @idTable1 AND status = 0
	
	
	IF (@idFirstBill IS NULL)
	BEGIN
		INSERT dbo.Bill
		        ( DateCheckIn ,
		          DateCheckOut ,
		          idTable ,
		          status
		        )
		VALUES  ( GETDATE() , -- DateCheckIn - date
		          NULL , -- DateCheckOut - date
		          @idTable1 , -- idTable - int
		          0  -- status - int
		        )
		        
		SELECT @idFirstBill = MAX(id) FROM dbo.Bill WHERE idTable = @idTable1 AND status = 0
		
	END
	
	SELECT @isFirstTablEmty = COUNT(*) FROM dbo.BillInfo WHERE idBill = @idFirstBill
	
	IF (@idSecondBill IS NULL)
	BEGIN
		INSERT dbo.Bill
		        ( DateCheckIn ,
		          DateCheckOut ,
		          idTable ,
		          status
		        )
		VALUES  ( GETDATE() , -- DateCheckIn - date
		          NULL , -- DateCheckOut - date
		          @idTable2 , -- idTable - int
		          0  -- status - int
		        )
SELECT @idSecondBill = MAX(id) FROM dbo.Bill WHERE idTable = @idTable2 AND status = 0
		
	END
	
	SELECT @isSecondTablEmty = COUNT(*) FROM dbo.BillInfo WHERE idBill = @idSecondBill

	SELECT id INTO IDBillInfoTable FROM dbo.BillInfo WHERE idBill = @idSecondBill
	
	UPDATE dbo.BillInfo SET idBill = @idSecondBill WHERE idBill = @idFirstBill
	
	UPDATE dbo.BillInfo SET idBill = @idFirstBill WHERE id IN (SELECT * FROM IDBillInfoTable)
	
	DROP TABLE IDBillInfoTable
	
	IF (@isFirstTablEmty = 0)
		--UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable2
		BEGIN
			UPDATE dbo.BillInfo SET idBill = @idSecondBill WHERE idBill = @idFirstBill
			UPDATE dbo.Bill SET idTable = @idTable2 WHERE id = @idFirstBill
        
			-- Update TableBida status for the old table
			UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable1
		END
	IF (@isSecondTablEmty= 0)
		--UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable1
		BEGIN
			DELETE FROM dbo.BillInfo WHERE id = @idFirstBill
			DELETE FROM dbo.Bill WHERE id = @idTable1
		END
END
GO

select * from Bill inner join BillInfo on Bill.id = BillInfo.idBill where idTable = 2
select * from Bill where idTable = 6
exec USP_SwitchTabel 6,2
delete Bill where idTable = 7
update Bill set idTable = 9 where idTable = 5
-- end Chuyển bàn

DELETE dbo.BillInfo
DELETE dbo.Bill

GO


CREATE PROC USP_GetListBillByDate
@checkIn date, @checkOut date
AS 
BEGIN
	SELECT t.name AS [Tên bàn], b.totalPrice AS [Tổng tiền], DateCheckIn AS [Ngày vào], DateCheckOut AS [Ngày ra], discount AS [Giảm giá]
	FROM dbo.Bill AS b,dbo.TableBida AS t
	WHERE DateCheckIn >= @checkIn AND DateCheckOut <= @checkOut AND b.status = 1
	AND t.id = b.idTable
END
GO

CREATE PROC USP_UpdateAccount
@userName NVARCHAR(100), @displayName NVARCHAR(100), @password NVARCHAR(100), @newPassword NVARCHAR(100)
AS
BEGIN
	DECLARE @isRightPass INT = 0
	
	SELECT @isRightPass = COUNT(*) FROM dbo.Account WHERE USERName = @userName AND PassWord = @password
	
	IF (@isRightPass = 1)
	BEGIN
		IF (@newPassword = NULL OR @newPassword = '')
		BEGIN
			UPDATE dbo.Account SET DisplayName = @displayName WHERE UserName = @userName
		END		
		ELSE
			UPDATE dbo.Account SET DisplayName = @displayName, PassWord = @newPassword WHERE UserName = @userName
	end
END
GO

CREATE TRIGGER UTG_DeleteBillInfo
ON dbo.BillInfo FOR DELETE
AS 
BEGIN
	DECLARE @idBillInfo INT
	DECLARE @idBill INT
	SELECT @idBillInfo = id, @idBill = Deleted.idBill FROM Deleted
	
	DECLARE @idTable INT
	SELECT @idTable = idTable FROM dbo.Bill WHERE id = @idBill
	
	DECLARE @count INT = 0
	
	SELECT @count = COUNT(*) FROM dbo.BillInfo AS bi, dbo.Bill AS b WHERE b.id = bi.idBill AND b.id = @idBill AND b.status = 0
	
	IF (@count = 0)
		UPDATE dbo.TableBida SET status = N'Trống' WHERE id = @idTable
END
GO

Select * from dbo.Account


Delete Account where UserName = N'quocne'

SELECT SERVERPROPERTY('IsFullTextInstalled')

--lưu trữ bill đã xóa
CREATE TABLE DeletedBills
(
    id INT,
    DateCheckIn DATETIME,
    DateCheckOut DATETIME,
    idTable INT,
    status INT,
	name NVARCHAR(100),
	count INT, 
	price float,
	totalPrice float
);
GO
select * from Bill
delete DeletedBills where id = 1
-- Tạo stored procedure để chuyển dữ liệu và hiển thị thông tin về hóa đơn đã xóa
GO
CREATE PROCEDURE USP_LogAndShowDeletedBills
AS
BEGIN

    -- Chuyển dữ liệu từ bảng BillInfo sang bảng ##DeletedBills\
	INSERT INTO DeletedBills (id, DateCheckIn, DateCheckOut, idTable, status, name, count, price, totalPrice)

    SELECT B.id, B.DateCheckIn, B.DateCheckOut, B.idTable, B.status, F.name, BI.count, F.price,  B.totalPrice
    FROM Bill B
    INNER JOIN BillInfo BI ON B.id = BI.idBill
    INNER JOIN Food F ON BI.idFood = F.id
    WHERE B.status = 1; -- Chỉ chọn những hóa đơn đã thanh toán (đã xóa)

    -- Hiển thị thông tin về hóa đơn đã xóa
    SELECT *	
    FROM DeletedBills;

    -- Xóa dữ liệu đã chuyển từ bảng BillInfo
    DELETE FROM BillInfo
    WHERE idBill IN (SELECT id FROM DeletedBills);

    -- Xóa dữ liệu đã chuyển từ bảng Bill
    DELETE FROM Bill
    WHERE status = 1; -- Xóa những hóa đơn đã thanh toán
END;
exec dbo.usp_LogAndShowDeletedBills
drop table DeletedBills

--Tạo proc doanh thu
CREATE PROCEDURE GetRevenueByDay
    @StartDate DATE,
    @EndDate DATE
AS
BEGIN
    -- Tính doanh thu theo ngày
    SELECT 
        CONVERT(DATE, DateCheckIn) AS SaleDate,
        SUM(price * count) AS DailyRevenue
    FROM 
        DeletedBills 
    WHERE 
        DateCheckIn BETWEEN @StartDate AND @EndDate
    GROUP BY 
        CONVERT(DATE, DateCheckIn)
    ORDER BY 
        SaleDate
END
GO
exec GetRevenueByDay '1-1-2023','2023-12-12'