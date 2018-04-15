package seu.simulation;

import seu.pojo.Snapshot;

import java.util.Collections;
import java.util.TreeMap;
import java.util.Vector;

import static seu.utility.ConfigUtil.*;

public class SimulateApp {

    public Vector<SimulateMessage> controllerMessageSequence = new Vector<>();
    private Vector<SimulateMessage> arrivedMessageSequence = new Vector<>();
    public TreeMap<Integer, Snapshot> snapshots = new TreeMap<>();

    public SimulateApp() throws Exception {
        // 生成 控制消息序列 快照 下标
        Vector<Integer> command2 = new Vector<>();
        while (command2.size() < SNAPSHOT_TIMES) {
            int index = RANDOM.nextInt(TRANSMIT_TIMES + SNAPSHOT_TIMES) + 1;
            if (command2.contains(index)) continue;
            command2.add(index);
        }
        // 生成 控制消息序列 命令 时间
        int absoluteTime = 0;
        for (int i = 0; i < TRANSMIT_TIMES + SNAPSHOT_TIMES + 1; i++) {
            // 最后 结束消息
            if (i == TRANSMIT_TIMES + SNAPSHOT_TIMES)
                controllerMessageSequence.add(new SimulateMessage(5, absoluteTime));
                // 根据 快照 下标 生成 快照消息
            else if (command2.contains(i))
                controllerMessageSequence.add(new SimulateMessage(2, absoluteTime));
                // 命令码1 资源转移消息
            else
                controllerMessageSequence.add(new SimulateMessage(1, absoluteTime));
            absoluteTime += getRandomInterval(5);
        }
        // TODO: 加入snapshot
        // 补全 控制消息序列 参数
        // 通过 控制消息序列 生成 全体消息序列
        int[] resources = new int[]{100, 100, 100};
        int newSnapshotId = 100;
        for (int i = 0; i < controllerMessageSequence.size() || arrivedMessageSequence.size() > 0; ) {
            // 处理在先发生的消息
            if (arrivedMessageSequence.size() > 0 &&
                    (i >= controllerMessageSequence.size() ||
                            controllerMessageSequence.elementAt(i).time >= arrivedMessageSequence.firstElement().time)) {
                SimulateMessage firstArrivedMessage = arrivedMessageSequence.firstElement();
                if (firstArrivedMessage.command == 3) { // 命令码 3 资源转移
                    // 修改资源
                    resources[char2int(firstArrivedMessage.to)] += firstArrivedMessage.resource;
                    // 监听通道 增加资源
                    for (Integer snapshotId : snapshots.keySet()) {
                        if (snapshots.get(snapshotId).isListen(firstArrivedMessage.from, firstArrivedMessage.to)) {
                            snapshots.get(snapshotId).addChannelResource(
                                    firstArrivedMessage.from,
                                    firstArrivedMessage.to,
                                    firstArrivedMessage.resource);
                        }
                    }
                } else if (firstArrivedMessage.command == 4) { // 命令码 4 快照消息
                    // 结束 监听通道
                    snapshots.get(firstArrivedMessage.snapshotId).cancelListen(
                            firstArrivedMessage.from,
                            firstArrivedMessage.to);
                    // 未收到快照节点
                    if (!snapshots.get(firstArrivedMessage.snapshotId).getChecked(firstArrivedMessage.to)) {
                        // 标记快照
                        snapshots.get(firstArrivedMessage.snapshotId).setChecked(firstArrivedMessage.to, true);
                        // 填写资源
                        snapshots.get(firstArrivedMessage.snapshotId).setNodeResource(
                                firstArrivedMessage.to,
                                resources[char2int(firstArrivedMessage.to)]);
                        // 监听另一通道
                        snapshots.get(firstArrivedMessage.snapshotId).setListen(
                                getOtherNode(firstArrivedMessage.to, firstArrivedMessage.from),
                                firstArrivedMessage.to);
                        // 衍生 快照消息 快照到达
                        for (char otherNode : getOtherNodes(firstArrivedMessage.to)) {
                            SimulateMessage arrivedMessage = new SimulateMessage(
                                    4,
                                    firstArrivedMessage.time + getDelay(firstArrivedMessage.to, otherNode));
                            arrivedMessage.snapshotId = firstArrivedMessage.snapshotId;
                            arrivedMessage.setFromTo(firstArrivedMessage.to, otherNode);
                            addArrivedMessageSequence(arrivedMessage);
                        }
                    }
                }
                arrivedMessageSequence.removeElementAt(0);
            } else if (controllerMessageSequence.elementAt(i).command == 1) { // 命令码 1 发起资源转移
                // 生成 资源转移参数
                int fromInt = RANDOM.nextInt(3);
                char from = int2char(fromInt);
                int toInt;
                do {
                    toInt = RANDOM.nextInt(3);
                } while (toInt == fromInt);
                char to = int2char(toInt);
                int resource = RANDOM.nextInt(resources[fromInt]) / 4 + 1;
                // 修改 资源
                resources[fromInt] -= resource;
                // 补全 控制消息序列 资源转移 发出点 到达点 资源
                controllerMessageSequence.elementAt(i).setFromTo(from, to);
                controllerMessageSequence.elementAt(i).resource = resource;
                // 衍生 到达消息 资源转移
                SimulateMessage arrivedMessage = new SimulateMessage(
                        3,
                        controllerMessageSequence.elementAt(i).time + getDelay(from, to));
                arrivedMessage.from = from;
                arrivedMessage.to = to;
                arrivedMessage.resource = resource;
                addArrivedMessageSequence(arrivedMessage);
                // 收尾
                i++;
            } else if (controllerMessageSequence.elementAt(i).command == 2) { // 命令码 2 发起快照消息
                // 补全 控制消息序列 快照参数 开始点 快照编号
                int toInt = RANDOM.nextInt(3);
                char to = int2char(toInt);
                controllerMessageSequence.elementAt(i).setFromTo('c', to);
                controllerMessageSequence.elementAt(i).snapshotId = newSnapshotId;
                // 衍生 快照消息 快照到达
                for (char otherNode : getOtherNodes(to)) {
                    SimulateMessage arrivedMessage = new SimulateMessage(
                            4,
                            controllerMessageSequence.elementAt(i).time + getDelay(to, otherNode));
                    arrivedMessage.snapshotId = newSnapshotId;
                    arrivedMessage.setFromTo(to, otherNode);
                    addArrivedMessageSequence(arrivedMessage);
                }
                // 增加 快照
                Snapshot snapshot = new Snapshot(newSnapshotId);
                snapshot.setNodeResource(to, resources[toInt]);
                snapshot.setChecked(to, true);
                for (char otherNode : getOtherNodes(to)) {
                    snapshot.setListen(otherNode, to);
                }
                snapshots.put(newSnapshotId, snapshot);
                // 收尾
                newSnapshotId++;
                i++;
            } else if (controllerMessageSequence.elementAt(i).command == 5) { // 命令码 5 结束程序
                i++;
            }
        }
    }

    private void addArrivedMessageSequence(SimulateMessage message) {
        arrivedMessageSequence.add(message);
        Collections.sort(arrivedMessageSequence);
    }

    private int char2int(char c) {
        switch (c) {
            case 'i':
                return 0;
            case 'j':
                return 1;
            case 'k':
                return 2;
            default:
                return -1;
        }
    }

    private char int2char(int i) {
        switch (i) {
            case 0:
                return 'i';
            case 1:
                return 'j';
            case 2:
                return 'k';
            default:
                return 0;
        }
    }
}
