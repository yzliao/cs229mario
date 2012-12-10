% The state numbers and corresponding descriptions:
% 0: small, nothing present
% 1: big, nothing present
% 2: fire, nothing present
% +8: jump height 1
% +16: jump height 2
% +24: jump height 3
% +32: enemy in ABOVE
% +64: enemy FRONT_ABOVE
% +128: enemy FRONT
% +256: enemy FRONT_BELOW
% +512: enemy BELOW
% +1024: enemy BEHIND
% +2048: front obstacle
% +4096: front obstacle 2
% +8192: front obstacle 3
% +16384: front obstacle 4

% Parse the Q table values for the given state and plot them.
function plotQvalues(states, numIter)
values = cell(length(states), 1);

%numFiles = numIter / 10;
numFiles = numIter;

for i = 1:length(states)
    values{i} = zeros(12, numFiles);
end
for i = 1:numFiles
    filename = sprintf('qt.%d.txt', (i - 1));
    lines = textread(filename, '%s', 'delimiter', '\n');
    for j = 1:length(lines)
        line = lines{j};
        %v = sscanf(line, '%d%*s%d%*s%d%*s%d%*s%d%*s%d%*s%d%*s%f');
        v = sscanf(line, '%d%*s%f%*s');
        for k = 1:length(states)
            state = states(k);
            if (v(1) == state)
                values{k}(:, i) = v(end-11:end)';
            end
        end
    end
end
% Read and plot the Qvalues for the input state.
for k = 1:length(states)
    figure
    hold on
    plot(values{k}', '--');
    ylabel('Q values')
    legendStr = {};
    for i = 1:12
        legendStr{i} = ['Action ', int2str(i)];
    end
    legend(legendStr);
    hold off
    title(['State ', int2str(states(k))]);
end

end