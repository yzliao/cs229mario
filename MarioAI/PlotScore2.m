%% PlotScore.m
clear all
%close all
clc

nTrainEachIter = 20;
nIter = 250;

% Load respective files.
load ../train_modified_level0/eval.txt
eval_original = eval(1:nIter, :);
load ../train_modified_level0_fixedalpha/eval.txt
eval_fixe_dalpha = eval(1:nIter, :);
load ../train_level0_gamma0_2/eval.txt
eval_low_gamma = eval(1:nIter, :);
load ../train_modified_level0/eval_rndSeed.txt

[m,n] = size(eval_rndSeed);
x = 0:nTrainEachIter:nTrainEachIter*(nIter - 1);


% Plot total score
figure(1)
grid on
hold on


filterLength = 20;
f = 1/filterLength * ones(filterLength, 1);

plot(x,eval_low_gamma(:,1),'y--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_low_gamma(:, 1));
plot(x-filterLength/2*nTrainEachIter, filtered1, '-', 'Linewidth', 3, 'color', [1 121/255 0]);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_fixe_dalpha(:,1),'c--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_fixe_dalpha(:, 1));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'b--', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_original(:,1),'m--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_original(:, 1));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'r-', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])


figure(1)
hold on
plot(ones(m,1)*x(end),eval_rndSeed(:,1),'kx', 'MarkerSize', 15, 'LineWidth', 2)


xlabel('Training Episodes', 'FontSize', 20);
ylabel('Evaluation Scores', 'FontSize', 20);
set(gca, 'XTick', [0:500:5000]);
set(gca,'FontSize',10);
legend('Learning Curve, low \gamma', 'Smoothed Learning Curve, low \gamma', ...
       'Learning Curve, fixed \alpha', 'Smoothed Learning Curve, fixed \alpha', ...
       'Learning Curve', 'Smoothed Learning Curve', 'Evaluation on Other Random Seeds', 'Location', 'SouthWest')
FigHandle = figure(1);
set(FigHandle, 'Position', [100, 100, 900, 600]);


%%
figure(2)
grid on
hold on


filterLength = 20;
f = 1/filterLength * ones(filterLength, 1);

plot(x,eval_low_gamma(:,2),'y--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_low_gamma(:, 2));
plot(x-filterLength/2*nTrainEachIter, filtered1, '-', 'Linewidth', 3, 'color', [1, 225/255, 51/255]);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_fixe_dalpha(:,2),'c--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_fixe_dalpha(:, 2));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'b--', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_original(:,2),'m--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_original(:, 2));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'r-', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

xlabel('Training Episodes', 'FontSize', 20);
ylabel('Winning Probability', 'FontSize', 20);
set(gca, 'XTick', [0:500:5000]);
set(gca,'FontSize',10);
legend('Low \gamma', 'Smoothed, low \gamma', ...
       'Fixed \alpha', 'Smoothed, fixed \alpha', ...
       'Our params', 'Smoothed', 'Location', 'SouthWest')
FigHandle = figure(2);
set(FigHandle, 'Position', [100, 100, 900, 600]);

%%
figure(3)
grid on
hold on


filterLength = 20;
f = 1/filterLength * ones(filterLength, 1);

plot(x,eval_low_gamma(:,3),'y--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_low_gamma(:, 3));
plot(x-filterLength/2*nTrainEachIter, filtered1, '-', 'Linewidth', 3, 'color', [1, 225/255, 51/255]);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_fixe_dalpha(:,3),'c--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_fixe_dalpha(:, 3));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'b--', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_original(:,3),'m--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_original(:, 3));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'r-', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

xlabel('Training Episodes', 'FontSize', 20);
ylabel('% of Monster Killed', 'FontSize', 20);
set(gca, 'XTick', [0:500:5000]);
set(gca,'FontSize',10);
legend('Low \gamma', 'Smoothed, low \gamma', ...
       'Fixed \alpha', 'Smoothed, fixed \alpha', ...
       'Our params', 'Smoothed', 'Location', 'SouthWest')
FigHandle = figure(3);
set(FigHandle, 'Position', [100, 100, 900, 600]);

%%
figure(4)
grid on
hold on


filterLength = 20;
f = 1/filterLength * ones(filterLength, 1);

plot(x,eval_low_gamma(:,5),'y--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_low_gamma(:, 5));
plot(x-filterLength/2*nTrainEachIter, filtered1, '-', 'Linewidth', 3, 'color', [1, 225/255, 51/255]);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_fixe_dalpha(:,5),'c--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_fixe_dalpha(:, 5));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'b--', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

plot(x,eval_original(:,5),'m--', 'Linewidth', 1)
filtered1 = filter(f, 1, eval_original(:, 5));
plot(x-filterLength/2*nTrainEachIter, filtered1, 'r-', 'Linewidth', 3);
xlim([0, nTrainEachIter * nIter])

xlabel('Training Episodes', 'FontSize', 20);
ylabel('Time Spent(frames)', 'FontSize', 20);
set(gca, 'XTick', [0:500:5000]);
set(gca,'FontSize',10);
legend('Low \gamma', 'Smoothed, low \gamma', ...
       'Fixed \alpha', 'Smoothed, fixed \alpha', ...
       'Our params', 'Smoothed', 'Location', 'SouthWest')
FigHandle = figure(4);
set(FigHandle, 'Position', [100, 100, 900, 600]);

%%
figure(3)
plot(x,eval_original(:,3),'k-h');
plot(x,eval_original(:,4),'g-s');
legend('','','')

%%
figure
plot(x,eval(:,5),'b-<');
%%
figure


grid on