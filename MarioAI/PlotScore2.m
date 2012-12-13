%% PlotScore.m
clear all
%close all
clc

nTrainEachIter = 20;
nIter = 250;

load ../train_modified_level0/eval.txt
%load ../eval.txt
load ../train_modified_level0/eval_rndSeed.txt
eval = eval(1:nIter, :);
[m,n] = size(eval_rndSeed);

x = 0:nTrainEachIter:nTrainEachIter*(nIter - 1);


% Plot total score
figure(1)
grid on
hold on
plot(x,eval(:,1),'b-', 'Linewidth', 2)

filterLength = 10;
f = 1/filterLength * ones(filterLength, 1);
filtered1 = filter(f, 1, eval(:, 1));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'r--', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

figure(1)
hold on
plot(ones(m,1)*x(end),eval_rndSeed(:,1),'kx', 'MarkerSize', 15, 'LineWidth', 2)

legend('Learning Curve', 'Smoothed Learning Curve', ...
      'Evaluation on Other Random Seeds', 'Location', 'SouthWest')
xlabel('Training Episodes', 'FontSize', 20);
ylabel('Evaluation Scores', 'FontSize', 20);
set(gca, 'XTick', [0:500:5000]);
set(gca,'FontSize',15);
FigHandle = figure(1);
set(FigHandle, 'Position', [100, 100, 900, 600]);


%%
figure
plot(x,eval(:,2),'r-o');
hold on
plot(x,eval(:,3),'k-h');
plot(x,eval(:,4),'g-s');
%%
figure
plot(x,eval(:,5),'b-<');
%%
figure


grid on