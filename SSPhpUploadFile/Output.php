<?php

/**
 * Json output
 */
class Output {
	var $status_code;
	var $message;
	var $data;
	function __construct() {
		$this->setHeader ();
	}
	function printSuccess($data) {
		$this->status_code = 200;
		$this->message = "Success"; 
		$this->data = $data;
		die ( json_encode ( $this ) );
	}
	function printError($message = "Error", $code = 404) {
		$this->status_code = $code;
		$this->message = $message;
		$this->data = [];
		die ( json_encode ( $this ) );
	}
	private function setHeader() {
		header ( 'Content-Type: application/json' );
	}
}
 