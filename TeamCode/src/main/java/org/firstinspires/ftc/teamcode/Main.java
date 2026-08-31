package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "main")
public class Main extends OpMode {
    private DcMotor motorIntake;

    static final int MAXIMO = 4270;
    static final double POWER = 1;

    private DcMotor motorViper;

    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;

    @Override
    public void init() {
        motorIntake = hardwareMap.get(DcMotor.class, "motorIntake");
        motorIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        motorIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorViper = hardwareMap.get(DcMotor.class, "viper");
        motorViper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorViper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftMotor = hardwareMap.dcMotor.get("lf");
        backLeftMotor = hardwareMap.dcMotor.get("lr");
        frontRightMotor = hardwareMap.dcMotor.get("rf");
        backRightMotor = hardwareMap.dcMotor.get("rr");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    /** This initializes the PoseUpdater, the drive motors, and the Panels telemetry. */
    @Override
    public void init_loop() {}

    @Override
    public void start() {}

    @Override
    public void loop() {
        // ======= INTAKE ======
        // Botão A: Liga o intake (gira para frente)
        if (gamepad1.a) {
            motorIntake.setPower(1.0);
        }

        // Botão X: Inverte o intake (gira pra trás)
        if (gamepad1.x) {
            motorIntake.setPower(-1.0);
        }

        // Botão B: Desliga o intake (para o motor)
        if (gamepad1.b) {
            motorIntake.setPower(0.0);  // 0.0 = parado
        }

        if(gamepad1.dpad_down) {
            motorViper.setTargetPosition(MAXIMO);
            motorViper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorViper.setPower(POWER);
        }

        if (gamepad1.b) {
            motorViper.setTargetPosition(0);
            motorViper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motorViper.setPower(POWER);
        }

        while (motorViper.isBusy()) {
            // Espera...
        }

        motorViper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorViper.setPower(0);

        double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = -gamepad1.right_stick_x;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }
}