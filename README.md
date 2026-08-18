# 💼 BaseBoss — Gestión Integral para Autónomos y Pequeños Profesionales

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-blue.svg" alt="Architecture" />
  <img src="https://img.shields.io/badge/Database-Room%20(SQLite)-red.svg" alt="Database" />
  <img src="https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-lightgrey.svg" alt="Min SDK" />
</p>

**BaseBoss** es una aplicación nativa para Android diseñada específicamente para autónomos, *freelancers* y profesionales independientes. Centraliza la facturación, el control de gastos, la cartera de clientes y el balance financiero en una única herramienta rápida, moderna y 100% funcional sin conexión a internet.

---

## 🚀 Características Principales

### 👥 Gestión de Clientes
* **Ficha completa:** Nombre/razón social, NIF/CIF/NIE, email, teléfono, dirección y notas.
* **Búsqueda en tiempo real:** Filtrado dinámico instantáneo por nombre o documento fiscal.
* **Histórico y protección:** Visualización de facturas emitidas por cliente e integridad referencial contra borrado accidental.

### 📄 Sistema de Facturación y Conceptos
* **Numeración correlativa automática:** Formato dinámico por series y año actual (ej. `F-2026-001`).
* **Cálculo reactivo y seguro:** Cálculo automático de bases imponibles, desglose de IVA y total sin errores de redondeo.
* **Gestión dinámica de líneas:** Añade múltiples conceptos con precio unitario, unidades y porcentaje de IVA variable.
* **Ciclo de vida comercial:** Estados de factura (*Borrador*, *Pendiente*, *Pagada*, *Vencida*, *Anulada*) con cambio rápido.

### 🖨️ Generación y Envío de Facturas en PDF
* **Exportación vectorial nativa:** Generación limpia de PDFs comerciales mediante la API nativa de Android (`PdfDocument`).
* **Diseño corporativo:** Incluye datos del emisor, datos del cliente, tabla de conceptos desglosada, total e IBAN de cobro.
* **Compartir al instante:** Integración con `FileProvider` e `Intent.ACTION_SEND` para enviar por WhatsApp, correo electrónico, Google Drive o imprimir directamente.

### 💸 Control de Gastos y Compras
* **Categorización profesional:** Material, Transporte, Software, Telefonía, Marketing, Formación, Equipamiento, Servicios y Otros.
* **Filtros rápidos:** Filtrado interactivo mediante *Chips* temáticos y fechas.
* **Cálculo de IVA soportado:** Control del impacto en el balance mensual.

### 📊 Dashboard y Métricas en Tiempo Real
* **KPIs clave del mes:** Ingresos cobrados, gastos registrados y beneficio neto estimado.
* **Contadores de control:** Facturas pendientes de cobro, facturas vencidas y total de clientes activos.
* **Gráfico comparativo nativo:** Histórico trimestral de ingresos vs. gastos renderizado mediante un componente personalizado sobre `Canvas`.

### ⚙️ Configuración y Perfil Fiscal
* Datos fiscales del profesional (nombre/empresa, NIF, dirección, contacto e IBAN).
* Sincronización automática de estos datos con cada factura PDF generada.

---

## 🛠️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura **MVVM (Model-View-ViewModel)** robusta, modular y desacoplada bajo el patrón **Single Activity**:

* **Lenguaje:** Java (compatibilidad Java 17).
* **Diseño e Interfaz:** XML nativo + Material Design 3.
* **Navegación:** Android Jetpack Navigation Component (`NavHostFragment` + `nav_graph.xml`).
* **Persistencia Local:** Android Jetpack Room (SQLite) con claves foráneas, índices y `TypeConverters`.
* **Flujos Reactivos:** `LiveData`, `MediatorLiveData` y `Transformations.switchMap`.
* **Concurrencia:** `ExecutorService` para operaciones de base de datos y procesamiento de PDFs fuera del hilo principal (`Main Thread`).
* **Manipulación de Documentos:** `android.graphics.pdf.PdfDocument` y `FileProvider`.

---

## 📂 Estructura del Proyecto

```text
com.example.baseboss/
│
├── BaseBossApp.java                # Inicialización global de la aplicación
├── MainActivity.java               # Single Activity contenedora del NavHost
│
├── datos/
│   ├── basedatos/
│   │   └── BaseBossDatabase.java   # Instancia Singleton de Room y ExecutorService
│   ├── dao/
│   │   ├── ClienteDao.java
│   │   ├── FacturaDao.java
│   │   ├── LineaFacturaDao.java
│   │   ├── GastoDao.java
│   │   └── ConfiguracionDao.java
│   └── entidades/
│       ├── Cliente.java
│       ├── Factura.java
│       ├── LineaFactura.java
│       ├── Gasto.java
│       ├── Configuracion.java
│       ├── FacturaConDetalles.java  # POJO de relación 1-N (Factura -> Líneas y Cliente)
│       └── ClienteConFacturas.java  # POJO de relación 1-N (Cliente -> Facturas)
│
├── repositorio/
│   ├── ClienteRepositorio.java
│   ├── FacturaRepositorio.java
│   ├── GastoRepositorio.java
│   ├── DashboardRepositorio.java
│   └── ConfiguracionRepositorio.java
│
├── ui/
│   ├── adaptadores/
│   │   ├── ClienteAdaptador.java
│   │   ├── FacturaAdaptador.java
│   │   ├── LineaFacturaFormularioAdaptador.java
│   │   └── GastoAdaptador.java
│   ├── clientes/
│   ├── facturas/
│   ├── gastos/
│   ├── dashboard/
│   │   └── GraficoBarrasTrimestral.java # Custom View para el gráfico de barras
│   └── configuracion/
│
└── utilidades/
    ├── ConversorFecha.java
    ├── FormateadorMoneda.java
    ├── Validador.java
    └── GeneradorPdfFactura.java
