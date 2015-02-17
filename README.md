Gangplank
=========

An open source data publishing platform

Investigating open source ways of publishing and visualising open data sets leveraging ElasticSearch and other open tools.


Database Setup

    mysql> create database gangplank default charset UTF8 default collate utf8_bin;
    mysql> grant all on gangplank.* to 'gangplank'@'localhost' identified by 'gangplank';
    mysql> grant all on gangplank.* to 'gangplank'@'localhost.localdomain' identified by 'gangplank';
    mysql> grant all on gangplank.* to 'gangplank'@'%' identified by 'gangplank';


Inspiration
https://data.cityofnewyork.us/City-Government/ECB-Notice-of-Violations/y6h5-jvss?
