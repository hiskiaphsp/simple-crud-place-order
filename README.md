# Simple CRUD Place Order API

This project is a simple Spring Boot REST API for managing customers, products, carts, and orders. The API allows users to create and update carts, place orders, and manage the relationships between customers, products, and their orders.

## Features

- **Customer Management**: Create and manage customer information.
- **Product Management**: Manage a product catalog with pricing and product details.
- **Cart Management**: Create carts, add products to a cart, and calculate total prices.
- **Order Management**: Place an order by converting a cart into an order and moving relevant cart data to the order.

## Endpoints

### Customer Endpoints
- **POST /api/customers**: Create a new customer.
- **GET /api/customers/{id}**: Get a customer by ID.

### Product Endpoints
- **GET /api/products**: Get a paginated list of products.
- **POST /api/products**: Add a new product.

### Cart Endpoints
- **POST /api/carts**: Create a new cart.
- **GET /api/carts/{id}**: Get a cart by ID.
- **PUT /api/carts/{id}**: Update a cart.
- **POST /api/carts/{id}/place**: Place an order from a cart.
- **DELETE /api/carts/{id}**: Delete a cart.

### Order Endpoints
- **GET /api/orders/{id}**: Get an order by ID.
- **POST /api/orders**: Create a new order (automatically when placing an order from a cart).

## Database Models

### Customer
- `id`: Long
- `name`: String
- `address`: String
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### Product
- `id`: Long
- `name`: String
- `description`: String
- `type`: String
- `price`: BigDecimal
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### Cart
- `id`: Long
- `customer`: Customer
- `status`: String (PENDING, PLACED)
- `totalPrice`: BigDecimal
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### CartDetail
- `id`: Long
- `product`: Product
- `quantity`: Integer
- `price`: BigDecimal
- `cart`: Cart
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### Order
- `id`: Long
- `customer`: Customer
- `status`: String (PLACED)
- `totalPrice`: BigDecimal
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

### OrderDetail
- `id`: Long
- `product`: Product
- `quantity`: Integer
- `price`: BigDecimal
- `order`: Order
- `createdAt`: Timestamp
- `updatedAt`: Timestamp

## Setup

1. Clone this repository.
2. Navigate to the project directory.
3. Install dependencies and build the project with Maven:
   ```bash
   mvn clean install
4. Run the application
5. The application will be available at http://localhost:8001
