<?php
// include 'DB.php';

include_once 'Output.php';

// function url(){
// 	return sprintf(
// 		"%s://%s",
// 		isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] != 'off' ? 'https' : 'http',
// 		$_SERVER['SERVER_NAME']
// 		//   $_SERVER['REQUEST_URI']

// 	);
// }


function url()
{
	return "http://192.168.1.7:8000";
}

class RequestMaster
{
	var $output = NULL;
	var $db = NULL;
	function __construct()
	{
		$this->output = new Output();
		// $this->db = new DB ( "localhost", "sale", "root", "" );
	}
	public function action()
	{
		if ($_SERVER['REQUEST_METHOD'] == "GET") {
			$url = isset($_GET['url']) ? $_GET['url'] : null;
			$url = rtrim($url, '/');
			$url = filter_var($url, FILTER_SANITIZE_URL);
			$url = explode('/', $url);
			// print_r( $url);

			if ($url[0] == "beatlist") {
				$outout = $this->db->getBeatList();
				$this->output->printSuccess($outout);
			} else if ($url[0] == "shoplist") {
				if (isset($url[1]) && $url[1] != "") {
					$outout = $this->db->getShopList($url[1]);
					$this->output->printSuccess($outout);
				} else {
					$this->output->printError();
				}
			} else if ($url[0] == "companycode") {
				$outout = $this->db->getCompanycode($url[0]);
				$this->output->printSuccess($outout);
			} else if ($url[0] == "itemlist") {
				if (isset($url[1]) && $url[1] != "") {
					$outout = $this->db->getItemList($url[1]);
					$this->output->printSuccess($outout);
				} else {
					$this->output->printError();
				}
			} else {
				echo "404";
				http_response_code(404);
			}
		} else if ($_SERVER['REQUEST_METHOD'] == "POST") {
			if ($_GET['url'] == "auth") {
				$postBody = file_get_contents("php://input");
				$postBody = json_decode($postBody);

				$authData = $this->db->userLogin($postBody->username, $postBody->password);
				if ($authData) {
					$this->output->printSuccess(array(
						"session" => $authData
					));
				} else {
					$this->output->printError("Unauthorized", 401);
				}
			}
			if ($_GET['url'] == "upload") {
				$file = $_FILES["file"]["name"];
				$ext = pathinfo($file, PATHINFO_EXTENSION);
				$folder = "files/";
				$path = $folder . md5(uniqid(rand(), true)) . "." . $ext; // New variable


				// print_r($ext);

				if (move_uploaded_file($_FILES["file"]["tmp_name"], $path)) {
					$this->output->printSuccess(array(
						"status" => 200,
						"file" => $path,
						"base_file" => url()
					));
				} else {
					$this->output->printError("File upload failed", 401);
				}
			}
		} else if ($_SERVER['REQUEST_METHOD'] == "DELETE") {
			http_response_code(405);
		} else {
			http_response_code(405);
		}
	}
}
$RequestMaster = new RequestMaster();
$RequestMaster->action();
