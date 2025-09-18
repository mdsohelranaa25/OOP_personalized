#include <bits/stdc++.h>
using namespace std;

int shortestCycle(int n, vector<int> adj[]) {
    int ans = INT_MAX;
    for(int start = 0; start < n; start++) {
        vector<int> dist(n, INT_MAX);
        vector<int> par(n, -1);
        queue<int> q;
        dist[start] = 0;
        q.push(start);

        while(!q.empty()) {
            
            int u = q.front(); q.pop();
            for(int v : adj[u]) {
                if(dist[v] == INT_MAX) {
                    dist[v] = dist[u] + 1;
                    par[v] = u;
                    q.push(v);
                } else if(par[u] != v) {
                    cerr<<u<< ' '<<v<<' '<<dist[u]<<' '<<dist[v]<<endl;
                    ans = min(ans, dist[u] + dist[v] + 1);
                }
            }
        }
         for(int i=1;i<=n;i++) cerr<<dist[i]<<' '<<i<<endl;
    }
   
    if(ans == INT_MAX) return -1;
    return ans;
}

int main() {
    int n, m;
    cin >> n >> m;
    vector<int> adj[n+5];
    for(int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    cout << shortestCycle(n, adj) << endl;
    
}
