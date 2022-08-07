# Introduction

This proposal outlines the ideas for RPC methods for use implementing a smart
automated environment in the domain of ‘Smart Farming’.The main areas
identified for potential automation on a farm are the milking process, the
feed levels, and the animal health and welfare.

The idea for the Milking Service is that sensors could be used in areas like the
milking service in order to identify the current cow being milked, as well
as the current capacity of the milk collection unit to know when it is full.
The service should be able to log the milk produced by a particular cow and
store the time, date and volume in a database.

With regard to the feed & water levels, the methods should be able to allow
monitoring of current feed available for the animals, as well as current
water available. This would allow for automated replenishing of these levels
as well as potentially automated generation of orders for new supplies as
the need arises.

Regarding the animal health service – the idea here is that it should be able to
generate a report for each animal based on the animal’s id, returning the
weight of the cow, average milk produced in the last week/month, etc. The
service should also be able to return similar information for the herd as a
whole, to give an indication of total consumption vs production.

## Service 1: Milking Service

This service deals with milking machine, allowing identification of current
cow being milked, current milk yield vs capacity and maintaining a log of
milk produced by each particular cow for use in both production predictions
and health assessment.

### 2.1 Methods

#### 2.1.1 RPC Method 1 – Milk Collection/Capacity

This method should be able to represent the total milk currently collected
in the collection unit relative to the capacity, to give an indication of
when the unit is full.This method will be implemented using Server Streaming.

#### 2.1.2 RPC Method 2 – Cow Milk Production Log

This method should be able to log the milk produced by the current milking
session against a particular cow, based on the ID of the current cow. It
should calculate the volume of milk by comparing the current total
collection against the last logged value and calculating the difference.This
method will be implemented using Unary Method.

#### 2.1.3RPC Method 3 – Current Cow Being Milked

This method should be capable of returning the ID of the current cow
being milked. This method will be implemented using Server Streaming.

## 3 Service 2: Feed Service

This service deals with monitoring feed and water levels, controlling the
replenishment of feed levels and the generation of a report to show feed
consumed within a particular time period.

### 3.1 Methods

#### 3.1.1 RPC Method 1 – Add to Current Feed Available

This method should allow the client to open a delivery chute for feed for
the animals. Further to this it should log the current weight before and
after delivery in order to log the amount consumed.This method will be
implemented using Bi Directional Streaming.

#### 3.1.2 RPC Method 2 – Current Water Available

This method should simply return the current water available by means of a
weight/volume sensor, for ensuring there is adequate water available.This
method will be implemented using Server Streaming.

#### 3.1.3 RPC Method 3 – Feed Consumption

This method should return a report outlining the feed consumed in a particular
time period. This could be used either for predicting supply levels or to
monitor animal health.This method will be implemented using Unary Method.

## 4 Service 3: Report Service

This service deals with reporting on individual cows as well as the herd as
a whole. This service could make use of the milking service also to pull
data about a particular cow or about the herd. In addition it could pull
data from the Feed Service about the herd regarding feed consumption.

### 4.1 Methods

#### 4.1.1 RPC Method 1 – Cow Report

This method should return a report about a particular cow, including
information about the amount of milk that particular cow has produced, it’s
current known weight, id etc. This method will be implemented usingClient
Streaming.

#### 4.1.2 RPC Method 2 – Herd Report

This method should return a report about the entire herd as a whole,
calculating the average milk produced per cow, the average feed consumed per
cow, as well as average statistics such as weight etc for a broad overview
of the herd.This method will be implemented using Client Streaming.   