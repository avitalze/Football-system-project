import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df_data = pd.read_csv('features.csv',sep=",")

# print(df_data.head(10))

"""" number of wins / loses / draws matches """
# names = ['win', 'lose', 'draw']
# result_freq = df_data['result'].value_counts()
# plt.figure(figsize=(9, 3))
# plt.subplot(131)
# plt.bar(names, result_freq)
# plt.suptitle('Number of wins/loses/draws Plotting')
# plt.show()


"""" wins per country """
# uniqe_country = df_data['country_id'].unique().astype(str)
# temp_result = np.array(df_data['result'])
# win_result = np.where(temp_result==-1,0,temp_result)
# newRes_country = pd.DataFrame(df_data['country_id'])
# newRes_country.insert(1,"result",win_result)
# newRes_country = newRes_country.groupby(["country_id"])["result"].count()
# # countries_id = ['1','1729','4769','4809','10257','13274','15722','17642','19694','21518','24558']
# # names = ['Belgium','England','France','Germany','Italy','Netherlands','Poland','Portugal','Scotland','Spain','Switzerland']
# plt.figure(figsize=(12, 5))
# plt.subplot(131)
# plt.subplots_adjust(0.125,0.1,1.9,0.9,0.2,0.2)
# # plt.tight_layout()
# plt.bar(uniqe_country, newRes_country)
# plt.suptitle('Wins per country Plotting')
# plt.show()


"""" wins per season """
# uniqe_season = df_data['season'].unique()
# temp_result = np.array(df_data['result'])
# win_result = np.where(temp_result==-1,0,temp_result)
# newRes_season = pd.DataFrame(df_data['season'])
# newRes_season.insert(1,"result",win_result)
# newRes_season = newRes_season.groupby(["season"])["result"].count()
#
# plt.figure(figsize=(12, 5))
# plt.subplot(131)
# plt.subplots_adjust(0.125,0.1,2.2,0.9,0.2,0.2)
# # plt.tight_layout()
# # plt.bar(seasons, newRes_season)
# plt.plot(uniqe_season, newRes_season)
# plt.suptitle('Wins per seasons Plotting')
# plt.show()

"""check per match - if home rating > away rating -> count home result, and opposite
check if overallrating could predict the result of match"""
def resultsPerRating():
    countWin = []
    countLose = []
    countDraw = []
    for row in df_data.index:
        line = df_data.iloc[df_data.index.get_loc(row):df_data.index.get_loc(row) + 1]
        if line['mean_overallrating_home_team'].values[0] > line['mean_overallrating_away_team'].values[0]:
            if line['result'].values[0] == 1:
                countWin.append('1')
            elif line['result'].values[0] == -1:
                countLose.append('1')
            else:
                countDraw.append('1')
        elif line['mean_overallrating_home_team'].values[0] < line['mean_overallrating_away_team'].values[0]:
            if line['result'].values[0] == 1:
                countLose.append('1')
            elif line['result'].values[0] == -1:
                countWin.append('1')
            else:
                countDraw.append('1')
    return [len(countWin), len(countLose), len(countDraw)]

def resultsPerRatingHome():
    countWin = []
    countLose = []
    countDraw = []
    for row in df_data.index:
        line = df_data.iloc[df_data.index.get_loc(row):df_data.index.get_loc(row) + 1]
        if line['mean_overallrating_home_team'].values[0] > line['mean_overallrating_away_team'].values[0]:
            if line['result'].values[0] == 1:
                countWin.append('1')
            elif line['result'].values[0] == -1:
                countLose.append('1')
            else:
                countDraw.append('1')
    return [len(countWin), len(countLose), len(countDraw)]

def resultsPerRatingAway():
    countWin = []
    countLose = []
    countDraw = []
    for row in df_data.index:
        line = df_data.iloc[df_data.index.get_loc(row):df_data.index.get_loc(row) + 1]
        if line['mean_overallrating_home_team'].values[0] < line['mean_overallrating_away_team'].values[0]:
            if line['result'].values[0] == 1:
                countLose.append('1')
            elif line['result'].values[0] == -1:
                countWin.append('1')
            else:
                countDraw.append('1')
    return [len(countWin), len(countLose), len(countDraw)]

result = resultsPerRating()
labels = ['Lose - Bad Prediction', 'Draw - 50%-50%', 'Win - Good Prediction']
res = {'result': ['1', '-1', '0'], 'Amount': result}
resultDF = pd.DataFrame(res).groupby(["result"])["Amount"].sum()
plt.figure(figsize=(12, 5))
plt.subplot(131)
plt.subplots_adjust(0.125, 0.1, 2.2, 0.9, 0.2, 0.2)
# plt.tight_layout()
# plt.bar(seasons, newRes_season)
plt.plot(labels, resultDF)
plt.suptitle('Wins/Lose/Draw prediction according to overallRating per team')
plt.show()
