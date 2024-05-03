INSERT INTO client ( id, name ) VALUES ( '1', 'Посольство РФ в Сербии' );
INSERT INTO client ( id, name ) VALUES ( '2', 'Федеральная антимонопольная служба' );
INSERT INTO client ( id, name ) VALUES ( '3', 'Otus family' );
INSERT INTO client ( id, name ) VALUES ( '4', 'Yandex family' );
INSERT INTO client ( id, name ) VALUES ( '5', 'Google family' );
SELECT setval( 'client_id_seq', 5 );

INSERT INTO address ( id, street, client_id ) VALUES ( '1', 'Србија, Београд, ул. Делиградска, 32', '1' );
INSERT INTO address ( id, street, client_id ) VALUES ( '2', 'Садовая-Кудринская, 11, Москва, Д-242', '2' );
INSERT INTO address ( id, street, client_id ) VALUES ( '3', 'г. Москва, Нарышкинская аллея., д. 5, стр. 2', '3' );
INSERT INTO address ( id, street, client_id ) VALUES ( '4', 'г. Москва, ул. Льва Толстого, 16', '4' );
INSERT INTO address ( id, street, client_id ) VALUES ( '5', '1600 Amphitheatre Parkway in Mountain View, California', '5' );
SELECT setval( 'address_id_seq', 5);

INSERT INTO phone ( id, number, client_id ) VALUES ( '1', '2-12-85-06', '1' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '2', '2-12-85-07', '1' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '3', '2-12-85-08', '1' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '4', '929-99-929', '2' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '5', '929-99-432', '2' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '6', '8-800-23-897', '3' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '7', '+9-929-89-89', '4' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '8', '+9-929-29-99', '4' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '9', '+9-929-99-99', '4' );
INSERT INTO phone ( id, number, client_id ) VALUES ( '10', '+100-500-25', '5' );
SELECT setval( 'phone_id_seq', 10 );
