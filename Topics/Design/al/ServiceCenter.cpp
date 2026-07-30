double getMinDistSum(vector<vector<int>>& positions) {
    int numPositions = positions.size();
  
    double currentX = 0.0, currentY = 0.0;
    for (const auto& position : positions) {
        currentX += position[0];
        currentY += position[1];
    }
    currentX /= numPositions;
    currentY /= numPositions;
  
    // Gradient descent parameters
    double decayRate = 0.999;          // Learning rate decay factor
    double convergenceThreshold = 1e-6; // Convergence criterion
    double learningRate = 0.5;          // Initial learning rate
  
    // Gradient descent optimization loop
    while (true) {
        double gradientX = 0.0, gradientY = 0.0;
        double totalDistance = 0.0;
      
        // Calculate gradient and total distance for current position
        for (const auto& position : positions) {
            double deltaX = currentX - position[0];
            double deltaY = currentY - position[1];
            double distance = sqrt(deltaX * deltaX + deltaY * deltaY);
          
            // Add small epsilon to avoid division by zero
            gradientX += deltaX / (distance + 1e-8);
            gradientY += deltaY / (distance + 1e-8);
            totalDistance += distance;
        }
      
        // Calculate the step size for this iteration
        double stepX = gradientX * learningRate;
        double stepY = gradientY * learningRate;
      
        // Check convergence: if step size is smaller than threshold, stop
        if (abs(stepX) <= convergenceThreshold && abs(stepY) <= convergenceThreshold) {
            return totalDistance;
        }
      
        // Update position using gradient descent
        currentX -= stepX;
        currentY -= stepY;
      
        // Decay the learning rate for next iteration
        learningRate *= decayRate;
    }
}
