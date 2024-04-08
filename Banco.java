import java.util.Scanner;

abstract class CuentaBancaria {
    protected String nombres;
    protected String apellidos;
    protected int edad;
    protected double saldo;

    public CuentaBancaria(String nombres, String apellidos, int edad, double montoApertura) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.saldo = montoApertura;
    }

    public abstract void realizarDeposito(double monto);

    public abstract void realizarRetiro(double monto);

    public void mostrarEstado() {
        System.out.println("\nCierre de mes (Estado de cuenta):");
        System.out.println("Cliente: " + nombres + " " + apellidos);
        System.out.println("Nombres: " + nombres);
        System.out.println("Apellidos: " + apellidos);
        System.out.println("Edad: " + edad);
        System.out.println("Saldo actual: $" + saldo);
    }
}

class CuentaAhorro extends CuentaBancaria {
    private final double tasaRendimientoAnual = 0.022;

    public CuentaAhorro(String nombres, String apellidos, int edad, double montoApertura) {
        super(nombres, apellidos, edad, montoApertura);
    }

    @Override
    public void realizarDeposito(double monto) {
        double comision = 0;

        if (monto < 500000) {
            comision = 7000;
        } else if (monto >= 500000 && monto < 2000000) {
            comision = 3000 + (monto * 0.01);
        } else if (monto >= 2000000 && monto <= 10000000) {
            comision = 2000 + (monto * 0.005);
        } else if (monto > 10000000) {
            comision = monto * 0.018;
        }

        saldo += monto - comision;
    }

    @Override
    public void realizarRetiro(double monto) {
        saldo -= monto;
    }

    public void aplicarInteresesMensuales() {
        double interesMensual = saldo * (tasaRendimientoAnual / 12);
        saldo += interesMensual;
    }
}

class CuentaCorriente extends CuentaBancaria {
    private final double tasaMantenimientoMensual = 0.015;
    private final double comisionCheque = 3000;

    public CuentaCorriente(String nombres, String apellidos, int edad, double montoApertura) {
        super(nombres, apellidos, edad, montoApertura);
    }

    @Override
    public void realizarDeposito(double monto) {
        double comision = 0;

        if (monto < 500000) {
            comision = 7000;
        } else if (monto >= 500000 && monto < 2000000) {
            comision = 5000 + (monto * 0.02);
        } else if (monto >= 2000000 && monto <= 10000000) {
            comision = 4000 + (monto * 0.02);
        } else if (monto > 10000000) {
            comision = monto * 0.033;
        }

        saldo += monto - comision;
    }

    @Override
    public void realizarRetiro(double monto) {
        saldo -= monto;
    }

    public void aplicarTasaMantenimiento() {
        double tasaMensual = saldo * tasaMantenimientoMensual;
        saldo -= tasaMensual;
    }

    public void cobrarComisionCheque() {
        saldo -= comisionCheque;
    }
}

public class Banco {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CuentaBancaria cuenta = null;

        System.out.println("------ Bienvenido al sistema bancario ------");

        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Aperturas de Cuentas: Ahorro y Corriente");
            System.out.println("2. Transferencias");
            System.out.println("3. Cajero Automático");
            System.out.println("4. Cierre de mes (Estado de Cuenta)");
            System.out.println("5. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("\nIngrese los datos del cliente:");
                    System.out.print("Nombres: ");
                    String nombres = scanner.nextLine();
                    System.out.print("Apellidos: ");
                    String apellidos = scanner.nextLine();
                    System.out.print("Edad: ");
                    int edad = scanner.nextInt();
                    System.out.print("Monto de apertura de cuenta: ");
                    double montoApertura = scanner.nextDouble();
                    System.out.print("Tipo de cuenta (1. Ahorro, 2. Corriente): ");
                    int tipoCuenta = scanner.nextInt();

                    if (tipoCuenta == 1) {
                        cuenta = new CuentaAhorro(nombres, apellidos, edad, montoApertura);
                        System.out.println("=> Cuenta de ahorro creada exitosamente.");
                    } else if (tipoCuenta == 2) {
                        cuenta = new CuentaCorriente(nombres, apellidos, edad, montoApertura);
                        System.out.println("=> Cuenta corriente creada exitosamente.");
                    } else {
                        System.out.println("=> Opción inválida.");
                    }
                    break;
                case 2:
                    if (cuenta != null) {
                        System.out.print("\nIngrese monto a transferir: ");
                        double montoTransferir = scanner.nextDouble();
                        cuenta.realizarDeposito(montoTransferir);
                        System.out.println("=> Transferencia realizada correctamente.\nSaldo: $:" + cuenta.saldo);
                    } else {
                        System.out.println("Debe abrir una cuenta primero.");
                    }
                    break;
                case 3:
                    if (cuenta != null) {
                        System.out.print("\nIngrese monto a retirar: ");
                        double montoRetirar = scanner.nextDouble();
                        cuenta.realizarRetiro(montoRetirar);
                        System.out.println("=> Retiro realizado correctamente.\nSaldo: $:" + cuenta.saldo);
                    } else {
                        System.out.println("Debe abrir una cuenta primero.");
                    }
                    break;
                case 4:
                    if (cuenta != null) {
                        if (cuenta instanceof CuentaAhorro) {
                            ((CuentaAhorro) cuenta).aplicarInteresesMensuales();
                        } else if (cuenta instanceof CuentaCorriente) {
                            ((CuentaCorriente) cuenta).aplicarTasaMantenimiento();
                        }
                        cuenta.mostrarEstado();
                    } else {
                        System.out.println("Debe abrir una cuenta primero.");
                    }
                    break;
                case 5:
                    System.out.println("------- ¡Hasta luego! -------");
                    return;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
    }
}
