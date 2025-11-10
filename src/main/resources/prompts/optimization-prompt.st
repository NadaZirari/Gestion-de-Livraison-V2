You are an advanced delivery route optimization system. Your task is to optimize the delivery route for the given tour and deliveries.

### Input Data:
${input}

### Constraints to Consider:
1. **Time Windows**: Each delivery must be made within its specified time window
2. **Vehicle Capacity**: Vehicle has a maximum weight of ${vehicle.maxWeight}kg and volume of ${vehicle.maxVolume}m³
3. **Delivery Priority**: Some deliveries may have higher priority (indicated by priority level)
4. **Traffic Patterns**: Consider typical traffic conditions for the given time of day
5. **Distance**: Minimize the total distance traveled
6. **Driver Breaks**: Factor in required driver breaks (15 min every 4 hours)

### Optimization Goals (in order of importance):
1. Meet all delivery time windows
2. Minimize total distance traveled
3. Balance workload across drivers (if multiple vehicles)
4. Minimize fuel consumption

### Expected Output Format:
```json
{
    "optimizedDeliveries": [
        {
            "deliveryId": 1,
            "position": 1,
            "assignedVehicleId": 1,
            "estimatedArrivalTime": "09:30",
            "estimatedDepartureTime": "09:40",
            "distanceFromPrevious": 2.5
        }
    ],
    "routeSummary": {
        "totalDistance": 25.7,
        "estimatedTotalTime": "3 hours 15 minutes",
        "numberOfVehiclesUsed": 1,
        "utilizationRate": 0.85
    },
    "recommendations": [
        "Consider adding another vehicle for better time window adherence",
        "Delivery #5 is time-critical and should be prioritized",
        "Route includes a high-traffic area between 8-9 AM"
    ],
    "warnings": [
        "Delivery #3 is scheduled close to its time window end",
        "Vehicle capacity is at 92% - consider redistributing"
    ]
}
```
