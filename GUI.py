import tkinter as tk


def create_board(parent, title):
    board_frame = tk.Frame(parent, padx=10, pady=10)

    lbl_title = tk.Label(
        board_frame,
        text=title,
        font=("Arial", 12, "bold")
    )
    lbl_title.grid(row=0, column=0, columnspan=11, pady=(0, 10))

    letters = ["A", "B", "C", "D", "E",
               "F", "G", "H", "I", "J"]

    # Nagłówki kolumn
    for col_idx, letter in enumerate(letters):
        lbl = tk.Label(
            board_frame,
            text=letter,
            width=3,
            font=("Arial", 9, "bold")
        )
        lbl.grid(row=1, column=col_idx + 1)

    buttons = {}

    # Wiersze 0-9
    for row_idx in range(10):

        # Numer wiersza 1-10
        lbl = tk.Label(
            board_frame,
            text=str(row_idx + 1),
            width=3,
            font=("Arial", 9, "bold")
        )
        lbl.grid(row=row_idx + 2, column=0)

        # Kolumny 0-9
        for col_idx in range(10):

            btn = tk.Button(
                board_frame,
                width=3,
                height=1,
                relief="raised",
                command=lambda r=row_idx, c=col_idx:
                    on_cell_click(title, r, c)
            )

            btn.grid(
                row=row_idx + 2,
                column=col_idx + 1,
                padx=1,
                pady=1
            )

            buttons[(row_idx, col_idx)] = btn

    return board_frame, buttons


def on_cell_click(board_name, row, col):
    print(f"Plansza: {board_name}")
    print(f"Wiersz: {row}")
    print(f"Kolumna: {col}")

    # Możesz teraz używać:
    # row = 0-9
    # col = 0-9


# Główne okno
root = tk.Tk()
root.title("Gra w Statki")
root.resizable(False, False)

# Plansza gracza
player_frame, player_buttons = create_board(
    root,
    "Twoja plansza"
)
player_frame.pack(
    side=tk.LEFT,
    padx=15,
    pady=15
)

# Plansza przeciwnika
enemy_frame, enemy_buttons = create_board(
    root,
    "Plansza przeciwnika"
)
enemy_frame.pack(
    side=tk.LEFT,
    padx=15,
    pady=15
)
message_label = tk.Label(
    root,
    text="",
    font=("Arial", 12, "bold")
)
message_label.pack()
def on_message(message):
    message_label.config(text=message)
def handle_message(message, row, col):
    if message == "trafiony":
        enemy_buttons[(row, col)].config(
            text="X",
            bg="red",
            fg="white",
            state="disabled"
        )

    elif message == "pudlo":
        enemy_buttons[(row, col)].config(
            text="O",
            bg="lightblue",
            state="disabled"
        )



root.mainloop()
