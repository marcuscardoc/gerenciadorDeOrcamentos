module pjtcurriculobvpisos {
	requires javafx.controls;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
