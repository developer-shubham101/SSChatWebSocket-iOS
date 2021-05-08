<?php
class DB {
	private $pdo;
	public function __construct($host, $dbname, $username, $password) {
		$pdo = new PDO ( 'mysql:host=' . $host . ';dbname=' . $dbname . ';charset=utf8', $username, $password );
		$pdo->setAttribute ( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
		$this->pdo = $pdo;
	}
	public function query($query, $params = array()) {
		$statement = $this->pdo->prepare ( $query );
		$statement->execute ( $params );
		
		if (explode ( ' ', $query ) [0] == 'SELECT') {
			$data = $statement->fetchAll ();
			return $data;
		}
	}
	public function getBeatList() {
		return $this->query ( "SELECT * FROM `beatmaster`" );
	}
	public function getShopList($beatCode) {
		return $this->query ( "SELECT * FROM `accountmaster` WHERE `beatcode` LIKE '$beatCode'" );
	}
	public function getCompanycode() {
		return $this->query ( "SELECT DISTINCT `companycode` FROM `itemmaster` WHERE 1" );
	}
	public function getItemList($companycode) {
		return $this->query ( "SELECT * FROM `itemmaster` WHERE `companycode` LIKE '$companycode'" );
	}
	public function userLogin($username, $password) {
		$user = $this->query ( "SELECT * FROM `userlogin` WHERE `UserName` = '$username' AND `Password` = '$password'" );
		if (empty ( $user )) {
			return false;
		} else {
			$userId = $user [0] ["USERID"];
			$count = $this->query ( "SELECT count(*) as count FROM `userSession` WHERE `userID` = '$userId'" );
			$session = md5 ( uniqid ( rand (), true ) );
			if ($count [0] ["count"]) {
				$sessionQuery = "UPDATE `userSession` set `session` = '$session',  `createdAt` = CURRENT_TIMESTAMP , `expireAt` = '2018-01-07 01:03:06' WHERE   `userID` = '$userId';";
				$this->query ( $sessionQuery );
			} else {
				$sessionQuery = "INSERT INTO `userSession` (`session`, `userID`, `createdAt`, `expireAt`) VALUES ('$session', '$userId', CURRENT_TIMESTAMP, '2018-01-05 01:03:06');";
				$this->query ( $sessionQuery );
			}
			return $session;
		}
	}
}
