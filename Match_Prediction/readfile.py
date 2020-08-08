import sqlite3
import pandas as pd
import numpy as np

from sklearn.metrics import accuracy_score
from sklearn.metrics import confusion_matrix

import matplotlib.pyplot as plt


# with sqlite3.connect('database.sqlite') as con:
#     countries = pd.read_sql_query("SELECT * from Country", con, index_col='id')
#     matches = pd.read_sql_query("SELECT * from Match", con, index_col='id')
#     leagues = pd.read_sql_query("SELECT * from League", con, index_col='id')
#     teams = pd.read_sql_query("SELECT * from Team", con, index_col='id')
#     players= pd.read_sql_query("SELECT * from Player", con, index_col='id')
#     player_stats_data = pd.read_sql("SELECT * FROM Player_Attributes;", con, index_col='id')
#
# print("done")





class ReadFile:

    def __init__(self) -> None:
       # self.df_teams = pd.read_csv("teams.csv")
       # self.df_countries = pd.read_csv("countries.csv")
       # self.df_players = pd.read_csv("players.csv")
       # self.df_leagues = pd.read_csv("leagues.csv")

       #self.df_player_stats = pd.read_csv("player_stats_data.csv")
       # self.df_matches = pd.read_csv("match.csv")

        # df_matches = pd.read_csv(r"C:\Users\avital\Downloads\matches.csv")
        # i add
        self.df_matches = pd.read_csv(r"C:\Users\avital\Downloads\matches.csv")
        self.df_player_stats = pd.read_csv(r"C:\Users\avital\Downloads\player_stats_data.csv")

        self.row_index=0;
        print("read done")


    def culcAllrating(self,match, players):
        self.row_index=self.row_index+1
        # Define variables
        date = pd.to_datetime(match['date'])
        allPlayersStas = pd.DataFrame()
        # Loop through all players
        for player in players:
            # id of player exist in data
            if np.isnan(player) == False:
                #  Get player stats
                stats = self.df_player_stats.loc[self.df_player_stats.player_api_id == player,['overall_rating']]#.sort_values(by = 'date', ascending = False)
                # Identify current stats
                #print( stats)
                #print(stats['overall_rating'].iloc[0])
                #stats = stats[['date', 'overall_rating']]
                #stats['date'] = pd.to_datetime(stats['date'])
                #current_stats = stats[stats['date'] < date]
                #sum= current_stats.sum()
                """"
                enter=pd.Series(stats.iloc[0])
                allPlayersStas=allPlayersStas.append(enter)
                """
                playerMean = stats.mean()
                allPlayersStas[player] = playerMean

            # id is empty - player not exist
            #else:
            #    current_stats = 0  # ??
        print(self.row_index)
        if allPlayersStas.empty:
            return -1
        else:
            #meanStas = allPlayersStas.mean(axis=1)
            # Return player stats
            mean = allPlayersStas.mean(axis=1)#allPlayersStas['overall_rating'].mean()
            print(mean)
           # return meanStas['overall_rating']
            return mean


    def culcAllratingHomeTeam(self,match):
        players = match[['home_player_1', 'home_player_2', 'home_player_3', "home_player_4", "home_player_5",
                         "home_player_6", "home_player_7", "home_player_8", "home_player_9", "home_player_10",
                         "home_player_11"]]

        return self.culcAllrating(match, players)


    ''' calculate away players stats for a given match. '''


    def culcAllratingAwayTeam(self,match):
        players = match[["away_player_1", "away_player_2", "away_player_3", "away_player_4",
                         "away_player_5", "away_player_6", "away_player_7", "away_player_8", "away_player_9",
                         "away_player_10", "away_player_11"]]

        return self.culcAllrating(match, players)


    def run(self):
    ## main ##
        features = self.df_matches.loc[:, 'id':'away_team_api_id'].copy()

        print("features build")
        """" add feature - result : win : 1->home, (-1)->away, 0->draw"""
        subGoals = np.sign(self.df_matches.home_team_goal - self.df_matches.away_team_goal)
        features['result'] = subGoals
        features.stage = features.stage.apply(lambda x: int(x))
        # matches = matches[matches.columns[:9]]

        print("add game result")
        """" add feature - mean overallrating home team - for home team"""
        homeTeamRating = self.df_matches.apply(lambda x: self.culcAllratingHomeTeam(x), axis=1)
        df = pd.DataFrame(homeTeamRating)
        features['mean_overallrating_home_team'] = homeTeamRating

        print("overall rating home team")
        """" add feature - mean overallrating away team - for away team"""
        homeTeamRating = self.df_matches.apply(lambda x: self.culcAllratingAwayTeam(x), axis=1)
        df = pd.DataFrame(homeTeamRating)
        features['mean_overallrating_away_team'] = homeTeamRating

        print("overall rating away team")
        """" add features - mean statistics of gambling Site about winners  """
        gamblingSite = self.df_matches.loc[:, 'B365H':'BSA'].copy()
        #  about-  home win odds
        homeTeam = gamblingSite.filter(regex='H$', axis=1)
        features['mean_gamblingSites_home_win'] = homeTeam.mean(axis=1)

        print("gambling rating home team")

        #  about-  away win odds
        awayTeam = gamblingSite.filter(regex='A$', axis=1)
        features['mean_gamblingSites_away_win'] = awayTeam.mean(axis=1)

        print("gambling rating away team")

        #  about-  draw odds
        drawOdds = gamblingSite.filter(regex='D$', axis=1)
        features['mean_gamblingSites_draw'] = drawOdds.mean(axis=1)

        print("gambling rating draw team")

        # features.to_csv("features.csv", encoding='utf-8', index=False)
        # features.to_dense().to_csv("submission.csv", index = False, sep=',', encoding='utf-8')


        features.to_csv("features.csv", index=False, header=True)
        # pd.read_csv("countries.csv")
        print(features.head())

reaf= ReadFile()
reaf.run()