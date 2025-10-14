@startuml
title Dental SaaS - Core Billing Domain

class Service {
+id: str
+name: str
+category: str
+code: str
+base_rate: float
+duration_minutes: int
+requires_authorization: bool
+description: str
+is_active: bool
}

class Rate {
+id: str
+service_id: str
+payer_type: str
+contract_id: str
+amount: float
+currency: str
+valid_from: date
+valid_to: date
+is_active: bool
}

class InvoiceItem {
+id: str
+invoice_id: str
+service_id: str
+description: str
+quantity: int
+unit_price: float
+total_price: float
+rate_id: str
+performed_at: datetime
+provider_id: str
}

class Invoice {
+id: str
+patient_id: str
+provider_id: str
+date_issued: datetime
+due_date: datetime
+status: str
+subtotal: float
+tax: float
+total: float
+currency: str
+payer: str
+contract_id: str
+notes: str
+created_at: datetime
+updated_at: datetime
}

class Payment {
+id: str
+invoice_id: str
+amount: float
+currency: str
+payment_method: str
+transaction_ref: str
+date: datetime
+status: str
+payer: str
+notes: str
+created_at: datetime
+updated_at: datetime
}

' Relaciones
Service "1" -- "0..*" Rate : defines >
Service "1" -- "1..*" InvoiceItem : < used in
Invoice "1" -- "0..*" InvoiceItem : contains >
Invoice "1" -- "0..*" Payment : < settled by

@enduml